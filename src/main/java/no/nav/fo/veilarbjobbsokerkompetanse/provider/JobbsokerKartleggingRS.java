package no.nav.fo.veilarbjobbsokerkompetanse.provider;

import no.nav.apiapp.feil.Feil;
import no.nav.apiapp.security.PepClient;
import no.nav.common.auth.SubjectHandler;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbjobbsokerkompetanse.Mapper;
import no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient;
import no.nav.fo.veilarbjobbsokerkompetanse.db.KartleggingDao;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Kartlegging;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.KartleggingDto;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.OppfolgingDto;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static java.util.Optional.ofNullable;
import static no.nav.apiapp.feil.FeilType.FINNES_IKKE;
import static no.nav.fo.veilarbjobbsokerkompetanse.Mapper.map;
import static no.nav.fo.veilarbjobbsokerkompetanse.Metrikker.opprettKartleggingMetrikk;

@Component
@Path("/")
@Produces("application/json")
public class JobbsokerKartleggingRS {

    public static final String FNR_QUERY_PARAM = "fnr";

    @Inject
    private AktorService aktorService;

    @Inject
    private KartleggingDao kartleggingDao;

    @Inject
    private OppfolgingClient oppfolgingClient;

    @Inject
    private PepClient pepClient;

    @Inject
    Provider<HttpServletRequest> requestProvider;

    @GET
    @Path("oppfolging")
    public OppfolgingDto aboutMe() {
        String fnr = getFnr();
        pepClient.sjekkLeseTilgangTilFnr(fnr);
        return new OppfolgingDto().setUnderOppfolging(oppfolgingClient.underOppfolging(fnr));
    }

    @GET
    @Path("hent")
    public KartleggingDto hentNyesteBesvarelseForAktor() {
        String fnr = getFnr();
        pepClient.sjekkLeseTilgangTilFnr(fnr);
        sjekkAtBrukerErUnderOppfolging(fnr);

        return kartleggingDao.fetchMostRecentByAktorId(getAktorId(fnr))
            .map(Mapper::map)
            .orElse(null); // 204
    }

    @POST
    @Path("opprett")
    public KartleggingDto opprettBesvarelse(KartleggingDto kartleggingDto) {
        String fnr = getFnr();
        pepClient.sjekkSkriveTilgangTilFnr(fnr);
        sjekkAtBrukerErUnderOppfolging(fnr);

        long id = kartleggingDao.create(getAktorId(fnr), map(kartleggingDto));
        Kartlegging kartlegging = kartleggingDao.fetchById(id);
        opprettKartleggingMetrikk(kartlegging);
        return map(kartlegging);
    }

    private void sjekkAtBrukerErUnderOppfolging(String fnr) {
        if (!oppfolgingClient.underOppfolging(fnr)) {
            throw new Feil(new BrukerIkkeUnderOppfolging());
        }
    }

    private String getFnr() {
        String fnr = requestProvider.get().getParameter(FNR_QUERY_PARAM);
        return ofNullable(fnr).orElseGet(() ->
            SubjectHandler.getIdent().orElseThrow(IllegalArgumentException::new)
        );
    }

    private String getAktorId(String fnr) {
        return aktorService.getAktorId(fnr).orElseThrow(this::fantIkkeAktor);
    }

    private Feil fantIkkeAktor() {
        return new Feil(FINNES_IKKE, "Finner ikke aktørId for gitt Fnr");
    }

}
