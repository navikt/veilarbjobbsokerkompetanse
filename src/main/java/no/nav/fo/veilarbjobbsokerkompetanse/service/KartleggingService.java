package no.nav.fo.veilarbjobbsokerkompetanse.service;

import no.nav.apiapp.feil.Feil;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient;
import no.nav.fo.veilarbjobbsokerkompetanse.db.KartleggingDao;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Kartlegging;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static no.nav.apiapp.feil.FeilType.FINNES_IKKE;
import static no.nav.apiapp.feil.FeilType.UGYLDIG_HANDLING;

@Component
public class KartleggingService {

    @Inject
    private AktorService aktorService;

    @Inject
    private KartleggingDao kartleggingDao;

    @Inject
    private OppfolgingClient oppfolgingClient;

    public void create(String fnr, Kartlegging kartlegging) {
        if (oppfolgingClient.underOppfolging(fnr)) {
            kartleggingDao.create(getAktorId(fnr), kartlegging);
        }
        else {
            throw new Feil(new BrukerIkkeUnderOppfolging());
        }
    }

    public Kartlegging fetchMostRecentByFnr(String fnr) {
        if (oppfolgingClient.underOppfolging(fnr)) {
            return kartleggingDao.fetchMostRecentByAktorId(getAktorId(fnr));
        }
        throw new Feil(new BrukerIkkeUnderOppfolging());
    }

    private String getAktorId(String fnr) {
        return aktorService.getAktorId(fnr).orElseThrow(this::fantIkkeAktor);
    }

    private Feil fantIkkeAktor() {
        return new Feil(FINNES_IKKE, "Finner ikke aktørId for gitt Fnr");
    }
}
