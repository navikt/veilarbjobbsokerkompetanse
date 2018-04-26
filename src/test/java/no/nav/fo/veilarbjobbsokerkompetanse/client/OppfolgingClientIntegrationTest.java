package no.nav.fo.veilarbjobbsokerkompetanse.client;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import no.nav.brukerdialog.security.oidc.SystemUserTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static no.nav.common.pact.PactUtil.dtoBody;
import static no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient.FNR_QUERY_PARAM;
import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.APPLICATION_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(PactConsumerTestExt.class)
public class OppfolgingClientIntegrationTest {

    private static final String VEILARBOPPFOLGING_API_PATH = "/veilarboppfolging/api";
    private static final String FNR = "12345678901";
    private static final String VEILARBOPPFOLGING = "veilarboppfolging";

    @Pact(provider = VEILARBOPPFOLGING, consumer = APPLICATION_NAME)
    public RequestResponsePact ikkeUnderOppfolgingPact(PactDslWithProvider builder) {
        return oppfolgingStatusPact(builder, new OppfolgingStatus().setUnderOppfolging(false), "person ikke under oppfølging");
    }

    @Test
    @PactTestFor(pactMethod = "ikkeUnderOppfolgingPact")
    public void ikkeUnderOppfolging(MockServer mockServer) {
        OppfolgingClient oppfolgingClient = buildClient(mockServer);
        assertThat(oppfolgingClient.underOppfolging(FNR)).isFalse();
    }

    @Pact(provider = VEILARBOPPFOLGING, consumer = APPLICATION_NAME)
    public RequestResponsePact underOppfolgingPact(PactDslWithProvider builder) {
        return oppfolgingStatusPact(builder, new OppfolgingStatus().setUnderOppfolging(true), "person under oppfølging");
    }

    @Test
    @PactTestFor(pactMethod = "underOppfolgingPact")
    public void underOppfolging(MockServer mockServer) {
        OppfolgingClient oppfolgingClient = buildClient(mockServer);
        assertThat(oppfolgingClient.underOppfolging(FNR)).isTrue();
    }

    private RequestResponsePact oppfolgingStatusPact(PactDslWithProvider builder, OppfolgingStatus oppfolgingStatus, String state) {
        return builder
                .given(state)
                .uponReceiving("hent status på person under oppfølging")
                .path(VEILARBOPPFOLGING_API_PATH + "/oppfolging")
                .matchQuery(FNR_QUERY_PARAM, "\\d{11}", FNR)
                .matchHeader(AUTHORIZATION,"Bearer .+")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(dtoBody(oppfolgingStatus))
                .toPact();
    }

    private OppfolgingClient buildClient(MockServer mockServer) {
        String url = mockServer.getUrl() + VEILARBOPPFOLGING_API_PATH;

        SystemUserTokenProvider systemUserTokenProvider = mock(SystemUserTokenProvider.class);
        when(systemUserTokenProvider.getToken()).thenReturn("testToken");
        SystemUserAuthorizationInterceptor systemUserAuthorizationInterceptor = new SystemUserAuthorizationInterceptor(systemUserTokenProvider);

        return new OppfolgingClient(url, systemUserAuthorizationInterceptor);
    }

}