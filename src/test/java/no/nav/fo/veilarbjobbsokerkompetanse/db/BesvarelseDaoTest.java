package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.apiapp.feil.Feil;
import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.*;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class BesvarelseDaoTest extends IntegrasjonsTest {

    private static final String AKTOR_ID = "123456";
    private static final Instant NOW = Instant.now();
    private static final Instant LATER = NOW.plus(1, DAYS);
    private static final boolean UNDER_OPPFOLGING = true;

    @Inject
    private BesvarelseDao besvarelseDao;

    @Transactional
    @Test
    public void testCreateAndFetch() {
        besvarelseDao.create(AKTOR_ID, UNDER_OPPFOLGING, besvarelse(NOW));
        Besvarelse result = besvarelseDao.fetchMostRecentByAktorId(AKTOR_ID);

        assertThat(result.getAktorId()).isEqualTo(AKTOR_ID);
        assertThat(result.getBesvarelseDato()).isEqualTo(NOW);
        assertThat(result.getRaad()).hasAtLeastOneElementOfType(Raad.class);
        assertThat(result.getRaad().get(0).getRaadAktiviteter()).hasAtLeastOneElementOfType(Aktivitet.class);
        assertThat(result.getRaad().get(0).getRaadAktiviteter().get(0).getTittel()).isEqualTo("AktivitetTittel");
        assertThat(result.getSvar()).hasAtLeastOneElementOfType(Svar.class);
        assertThat(result.getSvar().get(0).getTips()).isEqualTo("TIPS");
        assertThat(result.getSvar().get(0).getSvarAlternativ()).hasAtLeastOneElementOfType(SvarAlternativ.class);
    }

    @Transactional
    @Test
    public void testMostRecentBesvarelse() {
        besvarelseDao.create(AKTOR_ID, UNDER_OPPFOLGING, besvarelse(NOW));
        besvarelseDao.create(AKTOR_ID, UNDER_OPPFOLGING, besvarelse(LATER));

        Besvarelse besvarelse = besvarelseDao.fetchMostRecentByAktorId(AKTOR_ID);

        assertThat(besvarelse.getBesvarelseDato()).isEqualTo(LATER);
    }

    @Transactional
    @Test(expected = Feil.class)
    public void testNoBesvarelseFound() {
        besvarelseDao.fetchMostRecentByAktorId(AKTOR_ID);
    }

    private Besvarelse besvarelse(Instant besvarelseDato) {
        return Besvarelse.builder()
                .besvarelseDato(besvarelseDato)
                .svar(asList(svar(), svar()))
                .raad(asList(raad(), raad()))
                .build();
    }

    private Svar svar() {
        return Svar.builder()
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
