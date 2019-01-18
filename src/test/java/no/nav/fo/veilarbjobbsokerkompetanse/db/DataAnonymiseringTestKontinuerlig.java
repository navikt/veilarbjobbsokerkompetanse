package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DataAnonymiseringTestKontinuerlig extends IntegrasjonsTest {

    @Inject
    private Database db;

    @Inject
    private KartleggingDao kartleggingDao;

    @Before
    public void setUp() {
        opprettKartlegging(1, "0001", 0);
        opprettKartlegging(2, "0002", 0);
        opprettKartlegging(3, "anonym", 0);
    }

    @Transactional
    @Test
    public void testAnonymiserByAktorId() {

        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 1000);
        kartleggingDao.anonymiserByAktorId("0001", date1);
        List<Kartlegging> kartleggingList1 = db.query("SELECT * FROM KARTLEGGING", kartleggingDao::map);
        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 1).findFirst().get().getAktorId()).isEqualTo("anonym");
        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 2).findFirst().get().getAktorId()).isEqualTo("0002");
        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 3).findFirst().get().getAktorId()).isEqualTo("anonym");

        kartleggingDao.anonymiserByAktorId("0002", date2);
        List<Kartlegging> kartleggingList2 = db.query("SELECT * FROM KARTLEGGING", kartleggingDao::map);
        assertThat(kartleggingList2.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 1).findFirst().get().getAktorId()).isEqualTo("anonym");
        assertThat(kartleggingList2.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 2).findFirst().get().getAktorId()).isEqualTo("anonym");
        assertThat(kartleggingList2.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 3).findFirst().get().getAktorId()).isEqualTo("anonym");
    }

    @Test
    public void testEldreFeedElement() {

        long id = opprettKartlegging(4, "0004", 0);
        Kartlegging k = kartleggingDao.fetchById(id);
        Timestamp thresholdTimestamp = new Timestamp(k.getKartleggingTidspunkt().getTime() - 1);

        kartleggingDao.anonymiserByAktorId("0004", thresholdTimestamp);
        List<Kartlegging> kartleggingList1 = db.query("SELECT * FROM KARTLEGGING", kartleggingDao::map);

        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 1).findFirst().get().getAktorId()).isEqualTo("0001");
        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 2).findFirst().get().getAktorId()).isEqualTo("0002");
        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 3).findFirst().get().getAktorId()).isEqualTo("anonym");
        assertThat(kartleggingList1.stream().filter(kartlegging -> kartlegging.getKartleggingId() == 4).findFirst().get().getAktorId()).isEqualTo("0004");
    }

    private long opprettKartlegging(long kartleggingId, String aktorId, int oppfolgingStatus) {
        Kartlegging kartlegging = TestData.kartlegging();
        db.update("INSERT INTO KARTLEGGING (" +
                "kartlegging_id, " +
                "aktor_id, " +
                "under_oppfolging, " +
                "oppsummering, " +
                "oppsummering_key, " +
                "kartlegging_dato) " +
                "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)",
            kartleggingId,
            aktorId,
            oppfolgingStatus,
            kartlegging.getOppsummering(),
            kartlegging.getOppsummeringKey()
        );

        return kartleggingId;
    }
}
