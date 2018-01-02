package no.nav.fo.veilarbjobbsokerkompetanse.api;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/jobbsokerkartlegging")
@Produces("application/json")
public interface JobbsokerKartleggingRS {
    @GET
    JobbsokerKartleggingDTO hentNyesteJobbsokerKartlegging();

    @POST
    @Path("/opprett")
    JobbsokerKartleggingDTO opprettJobbsokerkartlegging(JobbsokerKartleggingDTO jobbsokerKartleggingDTO);
}
