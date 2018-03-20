package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Besvarelse;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Raad;
import org.junit.Test;

import javax.inject.Inject;
import java.time.Instant;

import static java.util.Collections.emptyList;

public class BesvarelseDaoTest extends IntegrasjonsTest {

    @Inject
    private BesvarelseDao besvarelseDao;

    @Test
    public void test() {

        besvarelseDao.create(besvarelse());
        Besvarelse result = besvarelseDao.fetchMostRecentByAktorId(2L);
        System.out.println(result);

    }

    private Besvarelse besvarelse() {

        Raad raad = Raad.builder()
                .besvarelseId(1L)
                .build();

        return Besvarelse.builder()
                .besvarelseId(1L)
                .aktorId(2L)
                .besvarelseDato(Instant.now())
                .underOppfolging(false)
                .svar(emptyList())
                .raad(emptyList())
                .build();

    }

}
