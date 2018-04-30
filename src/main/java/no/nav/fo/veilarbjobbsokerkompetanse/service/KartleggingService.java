package no.nav.fo.veilarbjobbsokerkompetanse.service;

import no.nav.apiapp.feil.Feil;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient;
import no.nav.fo.veilarbjobbsokerkompetanse.db.KartleggingDao;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Kartlegging;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static no.nav.apiapp.feil.Feil.Type.FINNES_IKKE;

@Component
public class KartleggingService {

    @Inject
    private AktorService aktorService;

    @Inject
    private KartleggingDao kartleggingDao;

    @Inject
    private OppfolgingClient oppfolgingClient;

    public void create(String fnr, Kartlegging kartlegging) {
        boolean underOppfolging = oppfolgingClient.underOppfolging(fnr);
        kartleggingDao.create(getAktorId(fnr), underOppfolging, kartlegging);
    }

    public Kartlegging fetchMostRecentByFnr(String fnr) {
        return kartleggingDao.fetchMostRecentByAktorId(getAktorId(fnr));
    }

    private String getAktorId(String fnr) {
        return aktorService.getAktorId(fnr).orElseThrow(this::fantIkkeAktor);
    }

    private Feil fantIkkeAktor() {
        return new Feil(FINNES_IKKE, "Finner ikke aktørId for gitt Fnr");
    }


}
