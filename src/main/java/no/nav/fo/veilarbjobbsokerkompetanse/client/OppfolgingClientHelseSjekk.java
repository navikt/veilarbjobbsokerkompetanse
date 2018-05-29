package no.nav.fo.veilarbjobbsokerkompetanse.client;

import no.nav.apiapp.selftest.Helsesjekk;
import no.nav.apiapp.selftest.HelsesjekkMetadata;
import no.nav.brukerdialog.security.oidc.SystemUserTokenProvider;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient.VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME;
import static no.nav.sbl.rest.RestUtils.withClient;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

public class OppfolgingClientHelseSjekk implements Helsesjekk {

    private String veilarboppfolgingPingUrl = getRequiredProperty(VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME) + "/ping";
    private final SystemUserTokenProvider systemUserTokenProvider = new SystemUserTokenProvider();

    @Override
    public void helsesjekk() throws Throwable {
        int status = withClient(c ->
                c.target(veilarboppfolgingPingUrl)
                        .request()
                        .header(AUTHORIZATION, "Bearer " + systemUserTokenProvider.getToken())
                        .get()
                        .getStatus());
        if (!(status >= 200 && status < 300)) {
            throw new IllegalStateException("HTTP status " + status);
        }
    }

    @Override
    public HelsesjekkMetadata getMetadata() {
        return new HelsesjekkMetadata(
                "veilarboppfolging",
                veilarboppfolgingPingUrl,
                "Ping av veilarboppfolging",
                true
        );
    }

}