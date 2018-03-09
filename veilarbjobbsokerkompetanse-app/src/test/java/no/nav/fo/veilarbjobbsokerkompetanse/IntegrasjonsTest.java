package no.nav.fo.veilarbjobbsokerkompetanse;

import no.nav.fo.veilarbjobbsokerkompetanse.db.DatabaseTestContext;
import no.nav.fo.veilarbjobbsokerkompetanse.db.JobbsokerKartleggingDAO;
import no.nav.testconfig.ApiAppTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

import static no.nav.fo.veilarbjobbsokerkompetanse.db.JobbsokerKartleggingDAO.JOBBSOKERKARTLEGGING;

public abstract class IntegrasjonsTest extends AbstractIntegrasjonsTest {

    @BeforeAll
    @BeforeClass
    public static void setupContext() {
        ApiAppTest.setupTestContext();
        DatabaseTestContext.setupInMemoryContext();
        setupContext(
                ApplicationConfig.class
        );
    }

    @AfterEach
    @After
    public void deleteTestData() {
        database.update("DELETE FROM " + JOBBSOKERKARTLEGGING);
    }

    private JdbcTemplate database = getBean(JdbcTemplate.class);
    protected JobbsokerKartleggingDAO jobbsokerKartleggingDAO = getBean(JobbsokerKartleggingDAO.class);

    protected String aktorId() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    protected JobbsokerKartlegging opprettJobbsokerKartlegging(String aktorId, Timestamp lagretTidspunkt) {
        return jobbsokerKartleggingDAO.opprettJobbsokerKartlegging(nyJobbsokerKartlegging(aktorId, lagretTidspunkt, nySporsmalOgSvar().toString(), nyRaad().toString()));
    }

    protected JobbsokerKartlegging opprettJobbsokerKartleggingMedLagretTidspunkt(Timestamp lagretTidspunkt) {
        return jobbsokerKartleggingDAO.opprettJobbsokerKartlegging(nyJobbsokerKartlegging(aktorId(), lagretTidspunkt, nySporsmalOgSvar().toString(), nyRaad().toString()));
    }

    protected JobbsokerKartlegging opprettJobbsokerKartleggingMedAktorId(String aktorId) {
        return jobbsokerKartleggingDAO.opprettJobbsokerKartlegging(nyJobbsokerKartlegging(aktorId, Timestamp.valueOf(LocalDateTime.now()), nySporsmalOgSvar().toString(), nyRaad().toString()));
    }

    protected JobbsokerKartlegging opprettJobbsokerKartleggingMedBesvarelse(String besvarelse) {
        return jobbsokerKartleggingDAO.opprettJobbsokerKartlegging(nyJobbsokerKartlegging(aktorId(), Timestamp.valueOf(LocalDateTime.now()), besvarelse, nyRaad().toString()));
    }

    protected JobbsokerKartlegging opprettJobbsokerKartleggingMedRaad(String raad) {
        return jobbsokerKartleggingDAO.opprettJobbsokerKartlegging(nyJobbsokerKartlegging(aktorId(), Timestamp.valueOf(LocalDateTime.now()), nySporsmalOgSvar().toString(), raad));
    }

    protected JobbsokerKartlegging nyJobbsokerKartlegging(String aktorId,
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

    protected JSONObject nyRaad() {
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

    protected JSONObject nySporsmalOgSvar() {
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
