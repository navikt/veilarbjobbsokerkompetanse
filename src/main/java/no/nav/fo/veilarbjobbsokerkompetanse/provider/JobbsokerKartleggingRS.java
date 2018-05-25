package no.nav.fo.veilarbjobbsokerkompetanse.provider;

import no.nav.apiapp.util.SubjectUtils;
import no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Kartlegging;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.KartleggingDto;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.OppfolgingDto;
import no.nav.fo.veilarbjobbsokerkompetanse.service.KartleggingService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static no.nav.fo.veilarbjobbsokerkompetanse.Mapper.map;
import static no.nav.fo.veilarbjobbsokerkompetanse.Metrikker.opprettKartleggingMetrikk;

@Component
@Path("/")
@Produces("application/json")
public class JobbsokerKartleggingRS {

    @Inject
    private KartleggingService kartleggingService;

    @Inject
    private OppfolgingClient oppfolgingClient;

    @GET
    @Path("oppfolging")
    public OppfolgingDto aboutMe() {
        return new OppfolgingDto().setUnderOppfolging(oppfolgingClient.underOppfolging(getFnr()));
    }

    @GET
    @Path("hent")
    public KartleggingDto hentNyesteBesvarelseForAktor() {
        return map(kartleggingService.fetchMostRecentByFnr(getFnr()));
    }

    @POST
    @Path("opprett")
    public KartleggingDto opprettBesvarelse(KartleggingDto kartleggingDto) {
        kartleggingService.create(getFnr(), map(kartleggingDto));
        Kartlegging kartlegging = kartleggingService.fetchMostRecentByFnr(getFnr());
        opprettKartleggingMetrikk(kartlegging);
        return map(kartlegging);
    }

    private String getFnr() {
        return SubjectUtils.getUserId().orElseThrow(IllegalArgumentException::new);
    }

}
