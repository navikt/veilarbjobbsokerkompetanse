import no.nav.dialogarena.config.fasit.FasitUtils;
import no.nav.dialogarena.config.fasit.ServiceUser;
import no.nav.dialogarena.config.util.Util;
import no.nav.fo.veilarbjobbsokerkompetanse.db.DatabaseTestContext;
import no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants;
import no.nav.testconfig.ApiAppTest;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static no.nav.apiapp.config.Konfigurator.AZUREAD_B2C_DISCOVERY_URL_PROPERTY_NAME;
import static no.nav.apiapp.config.Konfigurator.AZUREAD_B2C_EXPECTED_AUDIENCE_PROPERTY_NAME;
import static no.nav.dialogarena.config.fasit.FasitUtils.Zone.FSS;
import static no.nav.dialogarena.config.fasit.FasitUtils.getDefaultEnvironment;
import static no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient.VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.*;

public class MainTest {

    private static final String PORT = "8800";

    public static void main(String[] args) throws Exception {
        ApiAppTest.setupTestContext();
        DatabaseTestContext.setupContext(getProperty("database"));

        String securityTokenService = FasitUtils.getBaseUrl("securityTokenService", FSS);
        ServiceUser srvveilarbjobbsokerkompetanse = FasitUtils.getServiceUser("srvveilarbjobbsokerkompetanse", APPLICATION_NAME);

        setProperty(StsSecurityConstants.STS_URL_KEY, securityTokenService);
        setProperty(StsSecurityConstants.SYSTEMUSER_USERNAME, srvveilarbjobbsokerkompetanse.getUsername());
        setProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD, srvveilarbjobbsokerkompetanse.getPassword());

        setProperty(AKTOER_V2_ENDPOINTURL, "https://app-" + getDefaultEnvironment() + ".adeo.no/aktoerid/AktoerService/v2");
        setProperty(VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME, "http://localhost:8080/veilarboppfolging/api");
        setProperty(RUN_WITH_MOCKS, "true");

        ServiceUser azureADClientId = FasitUtils.getServiceUser("aad_b2c_clientid", APPLICATION_NAME);
        Util.setProperty(AZUREAD_B2C_DISCOVERY_URL_PROPERTY_NAME, FasitUtils.getBaseUrl("aad_b2c_discovery"));
        Util.setProperty(AZUREAD_B2C_EXPECTED_AUDIENCE_PROPERTY_NAME, azureADClientId.username);

        Main.main(PORT);
    }

}
