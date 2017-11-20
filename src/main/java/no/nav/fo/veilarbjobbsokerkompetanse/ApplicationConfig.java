package no.nav.fo.veilarbjobbsokerkompetanse;

import no.nav.apiapp.ApiApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static no.nav.apiapp.ApiApplication.Sone.FSS;

@Configuration
@Import({
        DemoRessurs.class
})
public class ApplicationConfig implements ApiApplication {

    @Override
    public Sone getSone() {
        return FSS;
    }

    @Override
    public String getApplicationName() {
        return APPLICATION_NAME;
    }

    public static final String APPLICATION_NAME = "veilarbjobbsokerkompetanse";

}