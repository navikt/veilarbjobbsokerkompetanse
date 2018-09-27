package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.*;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.DAYS;
import static no.nav.fo.veilarbjobbsokerkompetanse.TestData.kartlegging;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KartleggingDaoTest extends IntegrasjonsTest {

    private static final String FNR = "12345678910";
    private static final String AKTOR_ID = "123456";
    private static final Instant NOW = Instant.now();
    private static final Instant BEFORE = NOW.minus(1, DAYS);
    private static final Instant LATER = NOW.plus(1, DAYS);
    private static final String OPPSUMMERING = "Dette er en oppsummering";
    private static final String OPPSUMMERING_KEY = "oppsummering-key1";

    @Inject
    private KartleggingDao kartleggingDao;

    @Transactional
    @Test
    public void testCreateAndFetch() {
        kartleggingDao.create(AKTOR_ID, kartlegging());
        Kartlegging result = kartleggingDao.fetchMostRecentByAktorId(AKTOR_ID);

        assertThat(result.getAktorId()).isEqualTo(AKTOR_ID);
        assertThat(result.getKartleggingDato()).isNotNull();
        assertThat(result.getRaad()).hasAtLeastOneElementOfType(Raad.class);
        assertThat(result.getRaad().get(0).getRaadAktiviteter()).hasAtLeastOneElementOfType(Aktivitet.class);
        assertThat(result.getRaad().get(0).getRaadAktiviteter().get(0).getTittel()).isEqualTo("AktivitetTittel");
        assertThat(result.getBesvarelse()).hasAtLeastOneElementOfType(Besvarelse.class);
        assertThat(result.getBesvarelse().get(0).getTips()).isEqualTo("TIPS");
        assertThat(result.getBesvarelse().get(0).getSvarAlternativ()).hasAtLeastOneElementOfType(SvarAlternativ.class);
        assertThat(result.getKulepunkter()).hasAtLeastOneElementOfType(Kulepunkt.class);
        assertThat(result.getKulepunkter().get(0).getKulepunktKey()).isEqualTo("KULEPUNKT-KEY");
        assertThat(result.getOppsummering()).isEqualTo(OPPSUMMERING);
        assertThat(result.getOppsummeringKey()).isEqualTo(OPPSUMMERING_KEY);
    }

    @Test
    public void testCreateNarIkkeUnderOppfolging() {
        OppfolgingClient oppfolgingClient = mock(OppfolgingClient.class);
        when(oppfolgingClient.underOppfolging(FNR)).thenReturn(false);
    }

    @Transactional
    @Test
    public void testMostRecentBesvarelse() {
        kartleggingDao.create(AKTOR_ID, kartlegging());
        kartleggingDao.create(AKTOR_ID, kartlegging());

        Kartlegging kartlegging = kartleggingDao.fetchMostRecentByAktorId(AKTOR_ID);

        assertThat(kartlegging.getKartleggingDato()).isBetween(BEFORE, LATER);
    }

    @Transactional
    @Test(expected = WebApplicationException.class)
    public void testNoBesvarelseFound() {
        kartleggingDao.fetchMostRecentByAktorId(AKTOR_ID);
    }


}
