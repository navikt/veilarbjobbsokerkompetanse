package no.nav.fo.veilarbjobbsokerkompetanse.db.dao;

import lombok.val;
import no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging;
import no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartleggingDAO;
import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.equalTo;

public class JobbsokerKartleggingDAOTest extends IntegrasjonsTest {

    private JdbcTemplate database = getBean(JdbcTemplate.class);
    private JobbsokerKartleggingDAO jobbsokerKartleggingDAO = getBean(JobbsokerKartleggingDAO.class);

    @After
    public void deleteTestData() {
        database.update("DELETE FROM JOBBSOKERKOMPETANSE");
    }

    @Test
    public void opprette_og_hente_jobbsokerKartlegging() {
        val aktorId = RandomStringUtils.randomAlphanumeric(10);
        val jobbsokerKartleggingData = jobbsokerKartleggingDAO.opprettJobbsokerKartlegging(nyJobbsokerKartlegging(aktorId));

        List<JobbsokerKartlegging> jobbsokerKartlegginger = jobbsokerKartleggingDAO.hentJobbsokerKartleggingerForAktorId(aktorId);

        assertThat(jobbsokerKartlegginger, hasSize(1));
        assertThat(jobbsokerKartleggingData, equalTo(jobbsokerKartlegginger.get(0)));
    }

    private JobbsokerKartlegging nyJobbsokerKartlegging(String aktorId) {
        return JobbsokerKartlegging.builder()
            .id(new Random().nextLong())
            .aktorId(aktorId)
            .lagretTidspunkt(Timestamp.valueOf(LocalDateTime.now()))
            .besvarelse(nySporsmalOgSvar().toString())
            .raad(nyRaad().toString())
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
