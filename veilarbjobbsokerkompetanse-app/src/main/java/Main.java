import no.nav.apiapp.ApiApp;
import no.nav.dialogarena.aktor.AktorConfig;
import no.nav.fo.veilarbjobbsokerkompetanse.ApplicationConfig;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static no.nav.fo.veilarbjobbsokerkompetanse.ApplicationConfig.AKTOER_V2_ENDPOINTURL;

public class Main {

    public static void main(String... args) throws Exception {
        setProperty(AktorConfig.AKTOER_ENDPOINT_URL, getProperty(AKTOER_V2_ENDPOINTURL));
        ApiApp.startApp(ApplicationConfig.class, args);
    }

}
