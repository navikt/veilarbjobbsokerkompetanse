package no.nav.fo.veilarbjobbsokerkompetanse.service;

import no.nav.apiapp.feil.Feil;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient;
import no.nav.fo.veilarbjobbsokerkompetanse.db.BesvarelseDao;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Besvarelse;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static no.nav.apiapp.feil.Feil.Type.FINNES_IKKE;

@Component
public class BesvarelseService {

    @Inject
    private AktorService aktorService;

    @Inject
    private BesvarelseDao besvarelseDao;

    @Inject
    private OppfolgingClient oppfolgingClient;

    public void create(String fnr, Besvarelse besvarelse) {
        boolean underOppfolging = oppfolgingClient.underOppfolging(fnr);
        besvarelseDao.create(getAktorId(fnr), underOppfolging, besvarelse);
    }

    public Besvarelse fetchMostRecentByFnr(String fnr) {
        return besvarelseDao.fetchMostRecentByAktorId(getAktorId(fnr));
    }

    private String getAktorId(String fnr) {
        return aktorService.getAktorId(fnr).orElseThrow(this::fantIkkeAktor);
    }

    private Feil fantIkkeAktor() {
        return new Feil(FINNES_IKKE, "Finner ikke aktørId for gitt Fnr");
    }


}
