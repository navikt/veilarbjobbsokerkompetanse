package no.nav.fo.veilarbjobbsokerkompetanse;

import no.nav.brukerdialog.security.Constants;
import no.nav.brukerdialog.tools.SecurityConstants;
import no.nav.fasit.FasitUtils;
import no.nav.fasit.ServiceUser;
import no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig;
import no.nav.sbl.dialogarena.common.abac.pep.CredentialConstants;
import no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants;
import no.nav.sbl.util.EnvironmentUtils;
import no.nav.testconfig.util.Util;

import static java.lang.System.setProperty;
import static no.nav.brukerdialog.security.oidc.provider.AzureADB2CConfig.EXTERNAL_USERS_AZUREAD_B2C_DISCOVERY_URL;
import static no.nav.brukerdialog.security.oidc.provider.AzureADB2CConfig.EXTERNAL_USERS_AZUREAD_B2C_EXPECTED_AUDIENCE;
import static no.nav.fasit.FasitUtils.Zone.FSS;
import static no.nav.fasit.FasitUtils.getDefaultEnvironment;
import static no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient.VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.AKTOER_V2_ENDPOINTURL;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.RUN_WITH_MOCKS;
import static no.nav.sbl.dialogarena.common.abac.pep.service.AbacServiceConfig.ABAC_ENDPOINT_URL_PROPERTY_NAME;
import static no.nav.sbl.util.EnvironmentUtils.Type.PUBLIC;

public class TestContext {

    public static final String APPLICATION_NAME = "veilarbjobbsokerkompetanse";

    public static void setup() {
        EnvironmentUtils.setProperty(ApplicationConfig.RUN_WITH_MOCKS, "true", PUBLIC);

        String securityTokenService = FasitUtils.getBaseUrl("securityTokenService", FSS);
        ServiceUser srvveilarbjobbsokerkompetanse = FasitUtils.getServiceUser("srvveilarbjobbsokerkompetanse", APPLICATION_NAME);

        setProperty(StsSecurityConstants.STS_URL_KEY, securityTokenService);
        setProperty(StsSecurityConstants.SYSTEMUSER_USERNAME, srvveilarbjobbsokerkompetanse.getUsername());
        setProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD, srvveilarbjobbsokerkompetanse.getPassword());

        setProperty(AKTOER_V2_ENDPOINTURL, "https://app-" + getDefaultEnvironment() + ".adeo.no/aktoerid/AktoerService/v2");
        setProperty(VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME, "https://localhost.nav.no:8443/veilarboppfolging/api");
        setProperty(RUN_WITH_MOCKS, "true");

        setProperty(ABAC_ENDPOINT_URL_PROPERTY_NAME, FasitUtils.getRestService("abac.pdp.endpoint", getDefaultEnvironment()).getUrl());
        setProperty(CredentialConstants.SYSTEMUSER_USERNAME, srvveilarbjobbsokerkompetanse.getUsername());
        setProperty(CredentialConstants.SYSTEMUSER_PASSWORD, srvveilarbjobbsokerkompetanse.getPassword());

        String issoHost = FasitUtils.getBaseUrl("isso-host");
        String issoJWS = FasitUtils.getBaseUrl("isso-jwks");
        String issoISSUER = FasitUtils.getBaseUrl("isso-issuer");
        String issoIsAlive = FasitUtils.getBaseUrl("isso.isalive", FasitUtils.Zone.FSS);
        ServiceUser isso_rp_user = FasitUtils.getServiceUser("isso-rp-user", APPLICATION_NAME);
        String loginUrl = FasitUtils.getRestService("veilarblogin.redirect-url", getDefaultEnvironment()).getUrl();

        setProperty(Constants.ISSO_HOST_URL_PROPERTY_NAME, issoHost);
        setProperty(Constants.ISSO_RP_USER_USERNAME_PROPERTY_NAME, isso_rp_user.getUsername());
        setProperty(Constants.ISSO_RP_USER_PASSWORD_PROPERTY_NAME, isso_rp_user.getPassword());
        setProperty(Constants.ISSO_JWKS_URL_PROPERTY_NAME, issoJWS);
        setProperty(Constants.ISSO_ISSUER_URL_PROPERTY_NAME, issoISSUER);
        setProperty(Constants.ISSO_ISALIVE_URL_PROPERTY_NAME, issoIsAlive);
        setProperty(SecurityConstants.SYSTEMUSER_USERNAME, srvveilarbjobbsokerkompetanse.getUsername());
        setProperty(SecurityConstants.SYSTEMUSER_PASSWORD, srvveilarbjobbsokerkompetanse.getPassword());
        setProperty(ApplicationConfig.VEILARBLOGIN_REDIRECT_URL_URL, loginUrl);
        setProperty(Constants.OIDC_REDIRECT_URL_PROPERTY_NAME, loginUrl);

        ServiceUser azureADClientId = FasitUtils.getServiceUser("aad_b2c_clientid", APPLICATION_NAME);
        Util.setProperty(EXTERNAL_USERS_AZUREAD_B2C_DISCOVERY_URL, FasitUtils.getBaseUrl("aad_b2c_discovery"));
        Util.setProperty(EXTERNAL_USERS_AZUREAD_B2C_EXPECTED_AUDIENCE, azureADClientId.username);
    }

}
