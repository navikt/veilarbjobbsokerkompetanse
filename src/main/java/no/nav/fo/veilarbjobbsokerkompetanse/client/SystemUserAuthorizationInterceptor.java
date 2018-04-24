package no.nav.fo.veilarbjobbsokerkompetanse.client;



import no.nav.brukerdialog.security.oidc.SystemUserTokenProvider;
import no.nav.fo.feed.common.OutInterceptor;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Invocation;
import java.io.IOException;

public class SystemUserAuthorizationInterceptor implements ClientRequestFilter {

    private final SystemUserTokenProvider systemUserTokenProvider ;

    public SystemUserAuthorizationInterceptor() {
        this(new SystemUserTokenProvider());
    }

    SystemUserAuthorizationInterceptor(SystemUserTokenProvider systemUserTokenProvider) {
        this.systemUserTokenProvider = systemUserTokenProvider;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().putSingle("Authorization", "Bearer " + systemUserTokenProvider.getToken());
    }

}
