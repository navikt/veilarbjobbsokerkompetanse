package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.TestData;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Kartlegging;
import no.nav.sbl.jdbc.Database;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DataAnonymiseringTestInitiell extends IntegrasjonsTest {

    @Inject
    private Database db;

    @Inject
    private KartleggingDao kartleggingDao;

    @Transactional
    @Test
    public void testAnonymisering() {
        opprettKartlegging(1,"0001", false);
        opprettKartlegging(2,"0001", true);
        opprettKartlegging(3, "0002", true);
        opprettKartlegging(4,"0002", true);
        opprettKartlegging(5,"0002", false);

        db.update("UPDATE KARTLEGGING SET aktor_id = 'anonym' WHERE under_oppfolging = false");

        List<Kartlegging> kartleggingList = db.query("SELECT * FROM KARTLEGGING", kartleggingDao::map);

        assertThat(kartleggingList.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 1).findFirst().get().getAktorId()).isEqualTo("anonym");
        assertThat(kartleggingList.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 2).findFirst().get().getAktorId()).isEqualTo("0001");
        assertThat(kartleggingList.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 5).findFirst().get().getAktorId()).isEqualTo("anonym");
        assertThat(kartleggingList.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 3).findFirst().get().getAktorId()).isEqualTo("0002");
    }

    private void opprettKartlegging(long kartleggingId, String aktorId, boolean oppfolgingStatus) {
        Kartlegging kartlegging = TestData.kartlegging();
        db.update("INSERT INTO KARTLEGGING (" +
                        "kartlegging_id, " +
                        "aktor_id, " +
                        "under_oppfolging, " +
                        "oppsummering, " +
                        "oppsummering_key, " +
                        "kartlegging_dato) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                kartleggingId,
                aktorId,
                oppfolgingStatus,
                kartlegging.getOppsummering(),
                kartlegging.getOppsummeringKey(),
                Timestamp.from(kartlegging.getKartleggingDato())
        );
    }
}
