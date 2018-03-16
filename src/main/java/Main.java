import no.nav.apiapp.ApiApp;
import no.nav.fo.veilarbjobbsokerkompetanse.ApplicationConfig;

import static java.lang.System.setProperty;
import static no.nav.dialogarena.aktor.AktorConfig.AKTOER_ENDPOINT_URL;
import static no.nav.fo.veilarbjobbsokerkompetanse.ApplicationConfig.AKTOER_V2_ENDPOINTURL;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

public class Main {

    public static void main(String... args) throws Exception {
        setProperty(AKTOER_ENDPOINT_URL, getRequiredProperty(AKTOER_V2_ENDPOINTURL));
        ApiApp.startApp(ApplicationConfig.class, args);
    }

}
