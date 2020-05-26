import no.nav.apiapp.ApiApp;
import no.nav.common.utils.NaisUtils;
import no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig;
import no.nav.fo.veilarbjobbsokerkompetanse.config.DataSourceConfig;

import static java.lang.System.setProperty;
import static no.nav.brukerdialog.security.Constants.OIDC_REDIRECT_URL_PROPERTY_NAME;
import static no.nav.dialogarena.aktor.AktorConfig.AKTOER_ENDPOINT_URL;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.*;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.DataSourceConfig.VEILARBJOBBSOKERKOMPETANSEDB_PASSWORD;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.DataSourceConfig.VEILARBJOBBSOKERKOMPETANSEDB_USERNAME;
import static no.nav.metrics.MetricsConfig.SENSU_BATCHES_PER_SECOND_PROPERTY_NAME;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

public class Main {

    public static void main(String... args) throws Exception {

        NaisUtils.Credentials serviceuser_creds = NaisUtils.getCredentials("serviceuser_creds");
        setProperty("SRVVEILARBJOBBSOKERKOMPETANSE_USERNAME", serviceuser_creds.username);
        setProperty("SRVVEILARBJOBBSOKERKOMPETANSE_PASSWORD", serviceuser_creds.password);

        NaisUtils.Credentials oracle_creds_creds = NaisUtils.getCredentials("oracle_creds");
        setProperty(VEILARBJOBBSOKERKOMPETANSEDB_USERNAME, oracle_creds_creds.username);
        setProperty(VEILARBJOBBSOKERKOMPETANSEDB_PASSWORD, oracle_creds_creds.password);

        setProperty(AKTOER_ENDPOINT_URL, getRequiredProperty(AKTOER_V2_ENDPOINTURL));
        setProperty(OIDC_REDIRECT_URL_PROPERTY_NAME, getRequiredProperty(VEILARBLOGIN_REDIRECT_URL_URL));

        setProperty(SENSU_BATCHES_PER_SECOND_PROPERTY_NAME, "1");

        ApiApp.runApp(ApplicationConfig.class, args);
    }

}
