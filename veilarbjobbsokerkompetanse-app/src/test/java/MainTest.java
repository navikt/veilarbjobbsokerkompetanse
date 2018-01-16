import no.nav.dialogarena.config.fasit.FasitUtils;
import no.nav.dialogarena.config.fasit.ServiceUser;
import no.nav.fo.veilarbjobbsokerkompetanse.db.DatabaseTestContext;
import no.nav.testconfig.ApiAppTest;

import static no.nav.dialogarena.config.fasit.FasitUtils.getDefaultEnvironment;
import static no.nav.fo.veilarbjobbsokerkompetanse.ApplicationConfig.APPLICATION_NAME;

public class MainTest {

    public static final String TEST_PORT = "8800";

    public static void main(String[] args) throws Exception {
        DatabaseTestContext.setupContext(System.getProperty("database"));
        ApiAppTest.setupTestContext();
        String securityTokenService = FasitUtils.getBaseUrl(
            "securityTokenService",
            // FasitUtil klarer idag ikke å resolve dette korrekt for nais-applikasjoner
            getDefaultEnvironment(),
            "test.local"
        );
        ServiceUser srvveilarbjobbsokerkompetanse = FasitUtils.getServiceUser(
            "srvveilarbjobbsokerkompetanse",
            APPLICATION_NAME,
            // FasitUtil klarer idag ikke å resolve dette korrekt for nais-applikasjoner
            getDefaultEnvironment(),
            "test.local"
        );
        System.setProperty(Main.SECURITYTOKENSERVICE_URL, securityTokenService);
        System.setProperty(Main.SRVVEILARBJOBBSOKERKOMPETANSE_USERNAME, srvveilarbjobbsokerkompetanse.getUsername());
        System.setProperty(Main.SRVVEILARBJOBBSOKERKOMPETANSE_PASSWORD, srvveilarbjobbsokerkompetanse.getPassword());

        Main.main(TEST_PORT);
    }

}
