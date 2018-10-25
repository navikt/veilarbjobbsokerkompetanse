package no.nav.fo.veilarbjobbsokerkompetanse.client;

import no.nav.common.auth.SubjectHandler;
import no.nav.sbl.rest.RestUtils;
import org.springframework.stereotype.Component;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.HttpHeaders.COOKIE;
import static no.nav.brukerdialog.security.oidc.provider.AzureADB2CProvider.AZUREADB2C_OIDC_COOKIE_NAME;
import static no.nav.common.auth.SsoToken.Type.OIDC;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Component
public class OppfolgingClient {

    public static final String VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME = "VEILARBOPPFOLGINGAPI_URL";
    static final String FNR_QUERY_PARAM = "fnr";

    private final String veilarboppfolgingTarget;

    @SuppressWarnings("unused")
    OppfolgingClient() {
        this(getRequiredProperty(VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME));
    }

    OppfolgingClient(String veilarboppfolgingTarget) {
        this.veilarboppfolgingTarget = veilarboppfolgingTarget;
    }

    public boolean underOppfolging(String fnr) {
        OppfolgingStatus oppfolgingStatus = RestUtils.withClient(c -> c.target(veilarboppfolgingTarget + "/oppfolging")
                .queryParam(FNR_QUERY_PARAM, fnr)
                .request()
                .header(AUTHORIZATION, "Bearer " + SubjectHandler.getSsoToken(OIDC).orElseThrow(IllegalArgumentException::new))
                .get(OppfolgingStatus.class)
        );
        return oppfolgingStatus.isUnderOppfolging();
    }

}