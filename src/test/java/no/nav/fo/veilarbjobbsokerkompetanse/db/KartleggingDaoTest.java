package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.*;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class KartleggingDaoTest extends IntegrasjonsTest {

    private static final String AKTOR_ID = "123456";
    private static final Instant NOW = Instant.now();
    private static final Instant BEFORE = NOW.minus(1, DAYS);
    private static final Instant LATER = NOW.plus(1, DAYS);
    private static final boolean UNDER_OPPFOLGING = true;

    @Inject
    private KartleggingDao kartleggingDao;

    @Transactional
    @Test
    public void testCreateAndFetch() {
        kartleggingDao.create(AKTOR_ID, UNDER_OPPFOLGING, kartlegging());
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
    }

    @Transactional
    @Test
    public void testMostRecentBesvarelse() {
        kartleggingDao.create(AKTOR_ID, UNDER_OPPFOLGING, kartlegging());
        kartleggingDao.create(AKTOR_ID, UNDER_OPPFOLGING, kartlegging());

        Kartlegging kartlegging = kartleggingDao.fetchMostRecentByAktorId(AKTOR_ID);

        assertThat(kartlegging.getKartleggingDato()).isBetween(BEFORE, LATER);
    }

    @Transactional
    @Test(expected = WebApplicationException.class)
    public void testNoBesvarelseFound() {
        kartleggingDao.fetchMostRecentByAktorId(AKTOR_ID);
    }

    private Kartlegging kartlegging() {
        return Kartlegging.builder()
                .besvarelse(asList(besvarelse(), besvarelse()))
                .raad(asList(raad(), raad()))
                .kulepunkter(asList(kulepunkt(), kulepunkt()))
                .kartleggingDato(Instant.now())
                .build();
    }

    private Kulepunkt kulepunkt() {
        return Kulepunkt.builder()
            .kulepunktKey("KULEPUNKT-KEY")
            .kulepunktPrioritet("KULEPUNKT-PRIORITET")
            .kulepunkt("KULEPUNKT-TEKST-TEKST-TEKST")
            .build();
    }

    private Besvarelse besvarelse() {
        return Besvarelse.builder()
                .sporsmalKey("SPORSMAL-1-KEY")
                .sporsmal("SPORSMAL-1")
                .tipsKey("TIPS-1-KEY")
                .tips("TIPS")
                .svarAlternativ(asList(svarAlternativ(), svarAlternativ()))
                .build();
    }

    private SvarAlternativ svarAlternativ() {
        return SvarAlternativ.builder()
                .svarAlternativKey("SVARALTERNATIV-1-KEY")
                .svarAlternativ("SVARALTERNATIV-1")
                .build();
    }

    private Raad raad() {
        return Raad.builder()
                .raadKey("R1")
                .raadTittel("RaadTittel")
                .raadIngress("RaadIngress")
                .raadAktiviteter(asList(aktivitet(), aktivitet()))
                .build();
    }

    private Aktivitet aktivitet() {
        return Aktivitet.builder()
                .tittel("AktivitetTittel")
                .innhold("AktivitetInnhold")
                .build();
    }

}
