package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.val;
import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static no.nav.fo.veilarbjobbsokerkompetanse.db.JobbsokerKartleggingDAO.JOBBSOKERKARTLEGGING;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JobbsokerKartleggingDAOTest extends IntegrasjonsTest {

    private JdbcTemplate database = getBean(JdbcTemplate.class);
    private JobbsokerKartleggingDAO jobbsokerKartleggingDAO = getBean(JobbsokerKartleggingDAO.class);

    @After
    public void deleteTestData() {
        database.update("DELETE FROM " + JOBBSOKERKARTLEGGING);
    }

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

    private String aktorId() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private JobbsokerKartlegging opprettJobbsokerKartlegging(String aktorId, Timestamp lagretTidspunkt) {
        return jobbsokerKartleggingDAO.opprettJobbsokerKartlegging(nyJobbsokerKartlegging(aktorId, lagretTidspunkt, nySporsmalOgSvar().toString(), nyRaad().toString()));
    }

    private JobbsokerKartlegging opprettJobbsokerKartleggingMedLagretTidspunkt(Timestamp lagretTidspunkt) {
        return jobbsokerKartleggingDAO.opprettJobbsokerKartlegging(nyJobbsokerKartlegging(aktorId(), lagretTidspunkt, nySporsmalOgSvar().toString(), nyRaad().toString()));
    }

    private JobbsokerKartlegging opprettJobbsokerKartleggingMedAktorId(String aktorId) {
        return jobbsokerKartleggingDAO.opprettJobbsokerKartlegging(nyJobbsokerKartlegging(aktorId, Timestamp.valueOf(LocalDateTime.now()), nySporsmalOgSvar().toString(), nyRaad().toString()));
    }

    private JobbsokerKartlegging opprettJobbsokerKartleggingMedBesvarelse(String besvarelse) {
        return jobbsokerKartleggingDAO.opprettJobbsokerKartlegging(nyJobbsokerKartlegging(aktorId(), Timestamp.valueOf(LocalDateTime.now()), besvarelse, nyRaad().toString()));
    }

    private JobbsokerKartlegging opprettJobbsokerKartleggingMedRaad(String raad) {
        return jobbsokerKartleggingDAO.opprettJobbsokerKartlegging(nyJobbsokerKartlegging(aktorId(), Timestamp.valueOf(LocalDateTime.now()), nySporsmalOgSvar().toString(), raad));
    }

    private JobbsokerKartlegging nyJobbsokerKartlegging(String aktorId,
                                                        Timestamp lagretTidspunkt,
                                                        String besvarelse,
                                                        String raad) {
        return JobbsokerKartlegging.builder()
            .id(new Random().nextLong())
            .aktorId(aktorId)
            .lagretTidspunkt(lagretTidspunkt)
            .besvarelse(besvarelse)
            .raad(raad)
            .build();
    }

    private JSONObject nyRaad() {
        JSONObject raad = new JSONObject();
        JSONObject raad1Data = new JSONObject();
        raad1Data.put("tittel", "Smil og vær glad");
        raad1Data.put("ingress", "Det er viktig å smile. Det blir man selv glad av og man sprer glede til andre.");
        JSONArray aktiviteterRaad1 = new JSONArray();
        JSONObject aktivitet1Raad1 = new JSONObject();
        aktivitet1Raad1.put("tittel", "Le");
        aktivitet1Raad1.put("innhold", "Le");
        JSONObject aktivitet2Raad1 = new JSONObject();
        aktivitet2Raad1.put("tittel", "Smil");
        aktivitet2Raad1.put("innhold", "Smil");
        aktiviteterRaad1.put(aktivitet1Raad1);
        aktiviteterRaad1.put(aktivitet2Raad1);
        raad1Data.put("aktiviteter", aktiviteterRaad1);
        raad.put("raad1", raad1Data);

        return raad;
    }

    private JSONObject nySporsmalOgSvar() {
        JSONObject spm = new JSONObject();
        JSONObject spm1Data = new JSONObject();
        JSONArray svarAlt1 = new JSONArray();
        svarAlt1.put("Familie");
        svarAlt1.put("Rikdom");
        svarAlt1.put("Makt");
        JSONArray svar1 = new JSONArray();
        svar1.put("Rikdom");
        spm1Data.put("sporsmal", "Hva er meningen med livet?");
        spm1Data.put("svar_alternativer", svarAlt1);
        spm1Data.put("svar", svar1);
        spm.put("sporsmal1", spm1Data);

        return spm;
    }
}
