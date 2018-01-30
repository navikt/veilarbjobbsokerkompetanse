import no.nav.dialogarena.aktor.AktorConfig;
import no.nav.dialogarena.config.fasit.FasitUtils;
import no.nav.dialogarena.config.fasit.ServiceUser;
import no.nav.fo.veilarbjobbsokerkompetanse.db.DatabaseTestContext;
import no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants;
import no.nav.testconfig.ApiAppTest;

import static no.nav.dialogarena.config.fasit.FasitUtils.getDefaultEnvironment;
import static no.nav.fo.veilarbjobbsokerkompetanse.ApplicationConfig.APPLICATION_NAME;

public class MainTest {

    public static final String TEST_PORT = "8800";

    public static void main(String[] args) throws Exception {
        ApiAppTest.setupTestContext();
        DatabaseTestContext.setupContext(System.getProperty("database"));

        String securityTokenService = FasitUtils.getBaseUrl("securityTokenService");
        ServiceUser srvveilarbjobbsokerkompetanse = FasitUtils.getServiceUser("srvveilarbjobbsokerkompetanse", APPLICATION_NAME);

        System.setProperty(StsSecurityConstants.STS_URL_KEY, securityTokenService);
        System.setProperty(StsSecurityConstants.SYSTEMUSER_USERNAME, srvveilarbjobbsokerkompetanse.getUsername());
        System.setProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD, srvveilarbjobbsokerkompetanse.getPassword());

        System.setProperty(AktorConfig.AKTOER_ENDPOINT_URL, "https://service-gw-" + getDefaultEnvironment() + ".test.local:443/");

        Main.main(TEST_PORT);
    }

}
