package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.fo.veilarbjobbsokerkompetanse.DbIntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.TestData;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Kartlegging;
import no.nav.sbl.jdbc.Database;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DataAnonymiseringTestInitiell extends DbIntegrasjonsTest {

    @Inject
    private Database db;

    private KartleggingDao kartleggingDao;

    @Before
    public void setup() {
        kartleggingDao = new KartleggingDao(db);
    }

    @Transactional
    @Test
    public void testAnonymisering() {
        Map<Integer, Long> map = new HashMap<>(5);
        map.put(1, opprettKartlegging("0001", false));
        map.put(2, opprettKartlegging("0001", true));
        map.put(3, opprettKartlegging("0002", true));
        map.put(4, opprettKartlegging("0002", true));
        map.put(5, opprettKartlegging("0002", false));

        db.update("UPDATE KARTLEGGING SET aktor_id = 'anonym' WHERE under_oppfolging = false");

        List<Kartlegging> kartleggingList = db.query("SELECT * FROM KARTLEGGING", kartleggingDao::map);

        assertThat(kartleggingList.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(1)).findFirst().get().getAktorId()).isEqualTo("anonym");
        assertThat(kartleggingList.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(2)).findFirst().get().getAktorId()).isEqualTo("0001");
        assertThat(kartleggingList.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(5)).findFirst().get().getAktorId()).isEqualTo("anonym");
        assertThat(kartleggingList.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(3)).findFirst().get().getAktorId()).isEqualTo("0002");
    }

    private Long opprettKartlegging(String aktorId, boolean oppfolgingStatus) {
        Kartlegging kartlegging = TestData.kartlegging();
        long _kartleggingId = db.nesteFraSekvens("KARTLEGGING_SEQ");

        db.update("INSERT INTO KARTLEGGING (" +
                        "kartlegging_id, " +
                        "aktor_id, " +
                        "under_oppfolging, " +
                        "oppsummering, " +
                        "oppsummering_key, " +
                        "kartlegging_dato) " +
                        "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)",
                _kartleggingId,
                aktorId,
                oppfolgingStatus,
                kartlegging.getOppsummering(),
                kartlegging.getOppsummeringKey()
        );

        return _kartleggingId;
    }
}
