package no.nav.fo.veilarbjobbsokerkompetanse;

import no.nav.brukerdialog.security.Constants;
import no.nav.brukerdialog.tools.SecurityConstants;
import no.nav.dialogarena.config.fasit.FasitUtils;
import no.nav.dialogarena.config.fasit.ServiceUser;
import no.nav.dialogarena.config.util.Util;
import no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants;

import static java.lang.System.setProperty;
import static no.nav.apiapp.config.Konfigurator.AZUREAD_B2C_DISCOVERY_URL_PROPERTY_NAME;
import static no.nav.apiapp.config.Konfigurator.AZUREAD_B2C_EXPECTED_AUDIENCE_PROPERTY_NAME;
import static no.nav.dialogarena.config.fasit.FasitUtils.Zone.FSS;
import static no.nav.dialogarena.config.fasit.FasitUtils.getDefaultEnvironment;
import static no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient.VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.AKTOER_V2_ENDPOINTURL;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.APPLICATION_NAME;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.RUN_WITH_MOCKS;

public class TestContext {

    public static void setup() {
        String securityTokenService = FasitUtils.getBaseUrl("securityTokenService", FSS);
        ServiceUser srvveilarbjobbsokerkompetanse = FasitUtils.getServiceUser("srvveilarbjobbsokerkompetanse", APPLICATION_NAME);

        setProperty(StsSecurityConstants.STS_URL_KEY, securityTokenService);
        setProperty(StsSecurityConstants.SYSTEMUSER_USERNAME, srvveilarbjobbsokerkompetanse.getUsername());
        setProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD, srvveilarbjobbsokerkompetanse.getPassword());

        setProperty(AKTOER_V2_ENDPOINTURL, "https://app-" + getDefaultEnvironment() + ".adeo.no/aktoerid/AktoerService/v2");
        setProperty(VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME, "http://localhost:8080/veilarboppfolging/api");
        setProperty(RUN_WITH_MOCKS, "true");


        String issoHost = FasitUtils.getBaseUrl("isso-host");
        String issoJWS = FasitUtils.getBaseUrl("isso-jwks");
        String issoISSUER = FasitUtils.getBaseUrl("isso-issuer");
        String issoIsAlive = FasitUtils.getBaseUrl("isso.isalive", FasitUtils.Zone.FSS);
        ServiceUser isso_rp_user = FasitUtils.getServiceUser("isso-rp-user", APPLICATION_NAME);
        String loginUrl = FasitUtils.getBaseUrl("veilarblogin.redirect-url", FasitUtils.Zone.FSS);

        setProperty(Constants.ISSO_HOST_URL_PROPERTY_NAME, issoHost);
        setProperty(Constants.ISSO_RP_USER_USERNAME_PROPERTY_NAME, isso_rp_user.getUsername());
        setProperty(Constants.ISSO_RP_USER_PASSWORD_PROPERTY_NAME, isso_rp_user.getPassword());
        setProperty(Constants.ISSO_JWKS_URL_PROPERTY_NAME, issoJWS);
        setProperty(Constants.ISSO_ISSUER_URL_PROPERTY_NAME, issoISSUER);
        setProperty(Constants.ISSO_ISALIVE_URL_PROPERTY_NAME, issoIsAlive);
        setProperty(SecurityConstants.SYSTEMUSER_USERNAME, srvveilarbjobbsokerkompetanse.getUsername());
        setProperty(SecurityConstants.SYSTEMUSER_PASSWORD, srvveilarbjobbsokerkompetanse.getPassword());
        setProperty(Constants.OIDC_REDIRECT_URL_PROPERTY_NAME, loginUrl);

        ServiceUser azureADClientId = FasitUtils.getServiceUser("aad_b2c_clientid", APPLICATION_NAME);
        Util.setProperty(AZUREAD_B2C_DISCOVERY_URL_PROPERTY_NAME, FasitUtils.getBaseUrl("aad_b2c_discovery"));
        Util.setProperty(AZUREAD_B2C_EXPECTED_AUDIENCE_PROPERTY_NAME, azureADClientId.username);
    }

}
