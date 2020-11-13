import no.nav.apiapp.ApiApp;
import no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.System.setProperty;
import static no.nav.brukerdialog.security.Constants.OIDC_REDIRECT_URL_PROPERTY_NAME;
import static no.nav.dialogarena.aktor.AktorConfig.AKTOER_ENDPOINT_URL;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.AKTOER_V2_ENDPOINTURL;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.VEILARBLOGIN_REDIRECT_URL_URL;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.DataSourceConfig.*;
import static no.nav.metrics.MetricsConfig.SENSU_BATCHES_PER_SECOND_PROPERTY_NAME;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) throws Exception {
        String serviceUserUsername = getVaultSecret("/var/run/secrets/nais.io/serviceuser_creds/username");
        String serviceUserPassword = getVaultSecret("/var/run/secrets/nais.io/serviceuser_creds/password");
        setProperty("SRVVEILARBJOBBSOKERKOMPETANSE_USERNAME", serviceUserUsername);
        setProperty("SRVVEILARBJOBBSOKERKOMPETANSE_PASSWORD", serviceUserPassword);

        String oracleUsername = getVaultSecret("/var/run/secrets/nais.io/oracle_creds/username");
        String oraclePassword = getVaultSecret("/var/run/secrets/nais.io/oracle_creds/password");
        setProperty(VEILARBJOBBSOKERKOMPETANSEDB_USERNAME, oracleUsername);
        setProperty(VEILARBJOBBSOKERKOMPETANSEDB_PASSWORD, oraclePassword);

        String jdbc_url = getVaultSecret("/var/run/secrets/nais.io/oracle_config/jdbc_url");
        setProperty(VEILARBJOBBSOKERKOMPETANSEDB_URL, jdbc_url);

        setProperty(AKTOER_ENDPOINT_URL, getRequiredProperty(AKTOER_V2_ENDPOINTURL));
        setProperty(OIDC_REDIRECT_URL_PROPERTY_NAME, getRequiredProperty(VEILARBLOGIN_REDIRECT_URL_URL));

        setProperty(SENSU_BATCHES_PER_SECOND_PROPERTY_NAME, "1");

        ApiApp.runApp(ApplicationConfig.class, args);
    }

    private static String getVaultSecret(String path) {
        try {
            return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Klarte ikke laste property fra vault for path: %s", path), e);
        }
    }

}
