package no.nav.fo.veilarbjobbsokerkompetanse.provider;

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
    public JobbsokerKartleggingDTO hentNyesteJobbsokerKartlegging() {
        return new JobbsokerKartleggingDTO()
                .setBesvarelse("Dette er en test");
    }

    @POST
    @Path("/opprett")
    public JobbsokerKartleggingDTO opprettJobbsokerkartlegging(JobbsokerKartleggingDTO jobbsokerKartleggingDTO) {
        return jobbsokerKartleggingDTO;
    }
}
