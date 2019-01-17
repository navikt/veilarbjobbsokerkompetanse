package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.*;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

import static no.nav.fo.veilarbjobbsokerkompetanse.TestData.kartlegging;
import static org.assertj.core.api.Assertions.assertThat;

public class KartleggingDaoTest extends IntegrasjonsTest {

    private static final String AKTOR_ID = "123456";
    private static final String OPPSUMMERING = "Dette er en oppsummering";
    private static final String OPPSUMMERING_KEY = "oppsummering-key1";

    @Inject
    private KartleggingDao kartleggingDao;

    public KartleggingDaoTest() {
        super(false);
    }

    @Transactional
    @Test
    public void testCreateAndFetch() {
        kartleggingDao.create(AKTOR_ID, kartlegging());
        Kartlegging result = kartleggingDao.fetchMostRecentByAktorId(AKTOR_ID).get();

        assertThat(result.getAktorId()).isEqualTo(AKTOR_ID);
        assertThat(result.getKartleggingTidspunkt()).isNotNull();
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

    @Transactional
    @Test
    public void testMostRecentBesvarelse() throws InterruptedException {

        kartleggingDao.create(AKTOR_ID, kartlegging());
        Thread.sleep(1);
        long id = kartleggingDao.create(AKTOR_ID, kartlegging());

        Kartlegging kartlegging = kartleggingDao.fetchMostRecentByAktorId(AKTOR_ID).get();

        assertThat(kartlegging.getKartleggingId()).isEqualTo(id);
    }

    @Transactional
    @Test
    public void testFetchById() {
        long id = kartleggingDao.create(AKTOR_ID, kartlegging());
        Kartlegging kartlegging = kartleggingDao.fetchById(id);
        assertThat(kartlegging.getKartleggingId()).isEqualTo(id);
    }

    @Transactional
    @Test
    public void testNoBesvarelseFound() {
        Optional<Kartlegging> kartlegging = kartleggingDao.fetchMostRecentByAktorId(UUID.randomUUID().toString());

        assertThat(kartlegging).isEmpty();
    }


}
