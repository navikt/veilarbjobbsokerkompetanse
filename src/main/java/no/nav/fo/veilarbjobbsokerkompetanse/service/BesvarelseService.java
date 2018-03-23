package no.nav.fo.veilarbjobbsokerkompetanse.service;

import no.nav.fo.veilarbjobbsokerkompetanse.db.BesvarelseDao;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Besvarelse;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class BesvarelseService {

    @Inject
    private BesvarelseDao besvarelseDao;

    public void create(String aktorId, Besvarelse besvarelse) {
        boolean underOppfolging = true; // TODO: query veilarboppfolging for this value.
        besvarelseDao.create(aktorId, underOppfolging, besvarelse);
    }

    public Besvarelse fetchMostRecentByAktorId(String aktorId) {
        return besvarelseDao.fetchMostRecentByAktorId(aktorId);
    }

}
