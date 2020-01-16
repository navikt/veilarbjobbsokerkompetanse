package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.fo.veilarbjobbsokerkompetanse.DbIntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.TestData;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Kartlegging;
import no.nav.sbl.jdbc.Database;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DataAnonymiseringTestKontinuerlig extends DbIntegrasjonsTest {

    @Inject
    private Database db;

    private KartleggingDao kartleggingDao;

    private Map<Integer, Long> map = new HashMap<>(3);

    @Before
    public void setup() {
        kartleggingDao = new KartleggingDao(db);

        long id1 = opprettKartlegging("0001", 0);
        map.put(1, id1);
        long id2 = opprettKartlegging("0002", 0);
        map.put(2, id2);
        long id3 = opprettKartlegging("anonym", 0);
        map.put(3, id3);
    }

    @Transactional
    @Test
    public void testAnonymiserByAktorId() {

        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 1000);
        kartleggingDao.anonymiserByAktorId("0001", date1);
        List<Kartlegging> kartleggingList1 = db.query("SELECT * FROM KARTLEGGING", kartleggingDao::map);
        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(1)).findFirst().get().getAktorId()).isEqualTo("anonym");
        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(2)).findFirst().get().getAktorId()).isEqualTo("0002");
        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(3)).findFirst().get().getAktorId()).isEqualTo("anonym");

        kartleggingDao.anonymiserByAktorId("0002", date2);
        List<Kartlegging> kartleggingList2 = db.query("SELECT * FROM KARTLEGGING", kartleggingDao::map);
        assertThat(kartleggingList2.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(1)).findFirst().get().getAktorId()).isEqualTo("anonym");
        assertThat(kartleggingList2.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(2)).findFirst().get().getAktorId()).isEqualTo("anonym");
        assertThat(kartleggingList2.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(3)).findFirst().get().getAktorId()).isEqualTo("anonym");
    }

    @Test
    public void testEldreFeedElement() {

        long id = opprettKartlegging("0004", 0);
        map.put(4, id);
        Kartlegging k = kartleggingDao.fetchById(id);
        Timestamp thresholdTimestamp = new Timestamp(k.getKartleggingTidspunkt().getTime() - 1);

        kartleggingDao.anonymiserByAktorId("0004", thresholdTimestamp);
        List<Kartlegging> kartleggingList1 = db.query("SELECT * FROM KARTLEGGING", kartleggingDao::map);

        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(1)).findFirst().get().getAktorId()).isEqualTo("0001");
        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(2)).findFirst().get().getAktorId()).isEqualTo("0002");
        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(3)).findFirst().get().getAktorId()).isEqualTo("anonym");
        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == map.get(4)).findFirst().get().getAktorId()).isEqualTo("0004");
    }

    private long opprettKartlegging(String aktorId, int oppfolgingStatus) {
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
