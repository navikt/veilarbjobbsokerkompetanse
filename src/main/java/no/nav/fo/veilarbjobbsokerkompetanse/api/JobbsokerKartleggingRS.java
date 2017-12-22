package no.nav.fo.veilarbjobbsokerkompetanse.api;

import no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/jobbsokerkartlegging")
@Produces("application/json")
public interface JobbsokerKartleggingRS {
    @GET
    JobbsokerKartlegging hentNyesteJobbsokerKartlegging();

    @POST
    @Path("/opprett")
    JobbsokerKartlegging opprettJobbsokerkartlegging(JobbsokerKartleggingDTO jobbsokerKartleggingDTO);
}
