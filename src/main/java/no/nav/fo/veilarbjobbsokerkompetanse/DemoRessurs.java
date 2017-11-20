package no.nav.fo.veilarbjobbsokerkompetanse;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Component
@Path("/")
public class DemoRessurs {

    @GET
    public String get() {
        return "alt ok!";
    }

}