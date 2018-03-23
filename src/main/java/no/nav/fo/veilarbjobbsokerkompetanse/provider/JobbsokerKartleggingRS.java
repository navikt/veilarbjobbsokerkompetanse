package no.nav.fo.veilarbjobbsokerkompetanse.provider;

import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.BesvarelseDto;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Component
@Path("/jobbsokerkartlegging")
@Produces("application/json")
public class JobbsokerKartleggingRS {

    @GET
    public BesvarelseDto hentNyesteBesvarelseForAktor() {
        return null;
    }

    @POST
    @Path("/opprett")
    public BesvarelseDto opprettBesvarelse(BesvarelseDto besvarelseDto) {
        return besvarelseDto;
    }
}
