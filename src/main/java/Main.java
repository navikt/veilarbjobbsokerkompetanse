import no.nav.apiapp.ApiApp;
import no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig;

import static java.lang.System.setProperty;
import static no.nav.dialogarena.aktor.AktorConfig.AKTOER_ENDPOINT_URL;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.AKTOER_V2_ENDPOINTURL;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

public class Main {

    public static void main(String... args) throws Exception {

        // TODO bør dette flyttes til enten baseimage eller api-app?
        System.setProperty("http.nonProxyHosts", "*.155.55.|*.192.168.|*.10.|*.local|*.rtv.gov|*.adeo.no|*.nav.no|*.aetat.no|*.devillo.no|*.oera.no");
        System.setProperty("http.proxyHost", "webproxy-utvikler.nav.no");
        System.setProperty("http.proxyPort", "8088");
        System.setProperty("https.proxyHost", "webproxy-utvikler.nav.no");
        System.setProperty("https.proxyPort", "8088");

        setProperty(AKTOER_ENDPOINT_URL, getRequiredProperty(AKTOER_V2_ENDPOINTURL));
        ApiApp.startApp(ApplicationConfig.class, args);
    }

}
