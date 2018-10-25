package no.nav.fo.veilarbjobbsokerkompetanse.provider;

import no.nav.apiapp.feil.IngenTilgang;
import no.nav.apiapp.security.PepClient;
import no.nav.brukerdialog.security.domain.IdentType;
import no.nav.common.auth.SubjectHandler;
import no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Kartlegging;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.KartleggingDto;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.OppfolgingDto;
import no.nav.fo.veilarbjobbsokerkompetanse.service.KartleggingService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.security.InvalidParameterException;

import static java.util.Optional.ofNullable;
import static no.nav.fo.veilarbjobbsokerkompetanse.Mapper.map;
import static no.nav.fo.veilarbjobbsokerkompetanse.Metrikker.opprettKartleggingMetrikk;

@Component
@Path("/")
@Produces("application/json")
public class JobbsokerKartleggingRS {

    public static final String FNR_QUERY_PARAM = "fnr";
    @Inject
    private KartleggingService kartleggingService;

    @Inject
    private OppfolgingClient oppfolgingClient;

    @Inject
    private PepClient pepClient;

    @Inject
    private Provider<HttpServletRequest> requestProvider;

    @GET
    @Path("oppfolging")
    public OppfolgingDto aboutMe() {
        String fnr = getFnr();
        pepClient.sjekkLeseTilgangTilFnr(fnr);
        return new OppfolgingDto().setUnderOppfolging(oppfolgingClient.underOppfolging(fnr));
    }

    @GET
    @Path("hent")
    public KartleggingDto hentNyesteBesvarelseForBruker() {
        skalVere(IdentType.EksternBruker);
        String fnr = SubjectHandler.getIdent().orElseThrow(NullPointerException::new);
        pepClient.sjekkLeseTilgangTilFnr(fnr);

        return map(kartleggingService.fetchMostRecentByFnr(fnr));
    }

    @GET
    @Path("hentSomInternBruker")
    public KartleggingDto hentNyesteBesvarelseForBrukerSomInternBruker() {
        skalVere(IdentType.InternBruker);

        String fnr = ofNullable(requestProvider.get().getParameter(FNR_QUERY_PARAM))
            .orElseThrow(InvalidParameterException::new);

        pepClient.sjekkLeseTilgangTilFnr(fnr);

        return map(kartleggingService.fetchMostRecentByFnr(fnr));
    }

    @POST
    @Path("opprett")
    public KartleggingDto opprettBesvarelse(KartleggingDto kartleggingDto) {
        String fnr = getFnr();
        pepClient.sjekkSkriveTilgangTilFnr(fnr);
        kartleggingService.create(fnr, map(kartleggingDto));
        Kartlegging kartlegging = kartleggingService.fetchMostRecentByFnr(fnr);
        opprettKartleggingMetrikk(kartlegging);
        return map(kartlegging);
    }

    private String getFnr() {
        String fnr = requestProvider.get().getParameter(FNR_QUERY_PARAM);
        return ofNullable(fnr).orElseGet(() ->
            SubjectHandler.getIdent().orElseThrow(IllegalArgumentException::new)
        );

    }

    private void skalVere(IdentType forventetIdentType) {
        IdentType identType = SubjectHandler.getIdentType().orElse(null);
        if (identType != forventetIdentType) {
            throw new IngenTilgang(String.format("%s != %s", identType, forventetIdentType));
        }
    }

}
