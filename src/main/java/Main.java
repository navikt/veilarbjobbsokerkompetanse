import no.nav.apiapp.ApiApp;
import no.nav.brukerdialog.security.context.JettySubjectHandler;
import no.nav.fo.veilarbjobbsokerkompetanse.ApplicationConfig;

import static java.lang.System.setProperty;
import static no.nav.brukerdialog.security.context.SubjectHandler.SUBJECTHANDLER_KEY;
import static no.nav.modig.security.sts.utility.STSConfigurationUtility.STS_URL_KEY;
import static no.nav.sbl.util.EnvironmentUtils.getOptionalProperty;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

public class Main {

    public static final String SECURITYTOKENSERVICE_URL = "SECURITYTOKENSERVICE_URL";
    public static final String SRVVEILARBJOBBSOKERKOMPETANSE_USERNAME = "SRVVEILARBJOBBSOKERKOMPETANSE_USERNAME";
    public static final String SRVVEILARBJOBBSOKERKOMPETANSE_PASSWORD = "SRVVEILARBJOBBSOKERKOMPETANSE_PASSWORD";
    public static final String TRUSTSTORE = "javax.net.ssl.trustStore";
    public static final String TRUSTSTOREPASSWORD = "javax.net.ssl.trustStorePassword";
    public static final String APP_TRUSTSTORE_PASSWORD = "APP_TRUSTSTORE_PASSWORD";

    public static void main(String... args) throws Exception {
        if (getOptionalProperty(TRUSTSTORE) == null) {
            System.setProperty(TRUSTSTORE, "/var/run/secrets/naisd.io/app_truststore_keystore");
        }
        if (getOptionalProperty(TRUSTSTOREPASSWORD) == null) {
            if (getRequiredProperty(APP_TRUSTSTORE_PASSWORD) == null) {
                throw new IllegalStateException("ENV APP_TRUSTSTORE_PASSWORD was not supplied");
            }
            System.setProperty(TRUSTSTOREPASSWORD, getRequiredProperty(APP_TRUSTSTORE_PASSWORD));
        }
        setProperty("java.security.egd", "file:/dev/./urandom");
        setProperty(STS_URL_KEY, getRequiredProperty(SECURITYTOKENSERVICE_URL));
        setProperty("no.nav.modig.security.systemuser.username", getRequiredProperty(SRVVEILARBJOBBSOKERKOMPETANSE_USERNAME));
        setProperty("no.nav.modig.security.systemuser.password", getRequiredProperty(SRVVEILARBJOBBSOKERKOMPETANSE_PASSWORD));
        setProperty(SUBJECTHANDLER_KEY, JettySubjectHandler.class.getName());

        ApiApp.startApp(ApplicationConfig.class, args);
    }

}
