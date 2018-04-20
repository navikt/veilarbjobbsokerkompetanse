package no.nav.fo.veilarbjobbsokerkompetanse.client;



import no.nav.brukerdialog.security.oidc.SystemUserTokenProvider;
import no.nav.fo.feed.common.OutInterceptor;

import javax.ws.rs.client.Invocation;

public class SystemUserAuthorizationInterceptor implements OutInterceptor {

    private SystemUserTokenProvider systemUserTokenProvider = new SystemUserTokenProvider();

    @Override
    public void apply(Invocation.Builder builder) {
        builder.header("Authorization", "Bearer " + systemUserTokenProvider.getToken());
    }

}
