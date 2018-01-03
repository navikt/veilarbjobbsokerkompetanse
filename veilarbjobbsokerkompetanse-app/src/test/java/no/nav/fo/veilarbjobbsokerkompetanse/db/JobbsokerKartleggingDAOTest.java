package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.val;
import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JobbsokerKartleggingDAOTest extends IntegrasjonsTest {

    @Test
    public void aaaaa_id_generator_leverer_korrekt_id() {
        long en = jobbsokerKartleggingDAO.getNextUniqueJobbsokerkompetanseId();
        long to = jobbsokerKartleggingDAO.getNextUniqueJobbsokerkompetanseId();
        long tre = jobbsokerKartleggingDAO.getNextUniqueJobbsokerkompetanseId();
        long fire = jobbsokerKartleggingDAO.getNextUniqueJobbsokerkompetanseId();
        long fem = jobbsokerKartleggingDAO.getNextUniqueJobbsokerkompetanseId();
        long seks = jobbsokerKartleggingDAO.getNextUniqueJobbsokerkompetanseId();
        long syv = jobbsokerKartleggingDAO.getNextUniqueJobbsokerkompetanseId();

        assertThat(en, equalTo(1L));
        assertThat(fire, equalTo(4L));
        assertThat(syv, equalTo(7L));
    }

    @Test
    public void lagretTidspunkt_blir_parset_korrekt() {
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        Timestamp timestampNow = Timestamp.valueOf(localDateTimeNow);

        JobbsokerKartlegging jobbsokerKartlegging = opprettJobbsokerKartlegging(aktorId(), timestampNow);

        assertThat(jobbsokerKartlegging.getLagretTidspunkt(), equalTo(timestampNow));
        assertThat(jobbsokerKartlegging.getLagretTidspunkt().toLocalDateTime(), equalTo(localDateTimeNow));
    }

    @Test
    public void opprette_og_hente_flere_jobbsokerKartlegginger() {
        String aktorId = aktorId();
        val jobbsokerKartlegging1 = opprettJobbsokerKartleggingMedAktorId(aktorId);
        val jobbsokerKartlegging2 = opprettJobbsokerKartleggingMedAktorId(aktorId);

        List<JobbsokerKartlegging> jobbsokerKartlegginger = jobbsokerKartleggingDAO.hentJobbsokerKartlegginger(aktorId);

        assertThat(jobbsokerKartlegginger, hasSize(2));
        assertThat(jobbsokerKartlegging1, equalTo(jobbsokerKartlegginger.get(0)));
        assertThat(jobbsokerKartlegging2, equalTo(jobbsokerKartlegginger.get(1)));
    }

    @Test
    public void opprette_og_hente_nyeste_jobbsokerKartlegging() {
        String aktorId = aktorId();
        val jobbsokerKartlegging1 = opprettJobbsokerKartlegging(aktorId, Timestamp.valueOf(LocalDateTime.now()));
        val jobbsokerKartlegging2 = opprettJobbsokerKartlegging(aktorId, Timestamp.valueOf(LocalDateTime.now().plusDays(10)));
        val jobbsokerKartlegging3 = opprettJobbsokerKartlegging(aktorId, Timestamp.valueOf(LocalDateTime.now().plusDays(5)));

        JobbsokerKartlegging nyesteJobbsokerKartlegging = jobbsokerKartleggingDAO.hentNyesteJobbsokerKartlegging(aktorId);

        assertThat(nyesteJobbsokerKartlegging, equalTo(jobbsokerKartlegging2));
    }

    @Test(expected = NullPointerException.class)
    public void oppretter_jobbsokerkartlegging_med_aktorId_null() {
        opprettJobbsokerKartleggingMedAktorId(null);
    }

    @Test(expected = NullPointerException.class)
    public void oppretter_jobbsokerkartlegging_med_lagretTidspunkt_null() {
        opprettJobbsokerKartleggingMedLagretTidspunkt(null);
    }

    @Test(expected = NullPointerException.class)
    public void oppretter_jobbsokerkartlegging_med_besvarelse_null() {
        opprettJobbsokerKartleggingMedBesvarelse(null);
    }

    @Test(expected = NullPointerException.class)
    public void oppretter_jobbsokerkartlegging_med_raad_null() {
        opprettJobbsokerKartleggingMedRaad(null);
    }
}
