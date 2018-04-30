package no.nav.fo.veilarbjobbsokerkompetanse.provider;

import no.nav.apiapp.util.SubjectUtils;
import no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.KartleggingDto;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.MeDto;
import no.nav.fo.veilarbjobbsokerkompetanse.service.KartleggingService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static no.nav.fo.veilarbjobbsokerkompetanse.Mapper.map;

@Component
@Path("/")
@Produces("application/json")
public class JobbsokerKartleggingRS {

    @Inject
    private KartleggingService kartleggingService;

    @Inject
    private OppfolgingClient oppfolgingClient;

    @GET
    @Path("me")
    public MeDto aboutMe() {
        return new MeDto().setUnderOppfolging(oppfolgingClient.underOppfolging(getFnr()));
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
        return map(kartleggingService.fetchMostRecentByFnr(getFnr()));
    }

    private String getFnr() {
        return SubjectUtils.getUserId().orElseThrow(IllegalArgumentException::new);
    }

}
