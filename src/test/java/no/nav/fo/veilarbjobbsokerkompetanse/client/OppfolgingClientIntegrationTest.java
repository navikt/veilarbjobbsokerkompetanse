package no.nav.fo.veilarbjobbsokerkompetanse.client;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import no.nav.common.auth.SsoToken;
import no.nav.common.auth.Subject;
import no.nav.common.auth.SubjectHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static no.nav.brukerdialog.security.domain.IdentType.EksternBruker;
import static no.nav.common.pact.PactUtil.dtoBody;
import static no.nav.fo.veilarbjobbsokerkompetanse.TestContext.APPLICATION_NAME;
import static no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient.FNR_QUERY_PARAM;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
public class OppfolgingClientIntegrationTest {

    private static final String VEILARBOPPFOLGING_API_PATH = "/veilarboppfolging/api";
    private static final String FNR = "12345678901";
    private static final String VEILARBOPPFOLGING = "veilarboppfolging";
    private static final SsoToken SSO_TOKEN = SsoToken.oidcToken("testToken");

    @Pact(provider = VEILARBOPPFOLGING, consumer = APPLICATION_NAME)
    public RequestResponsePact ikkeUnderOppfolgingPact(PactDslWithProvider builder) {
        return oppfolgingStatusPact(builder, new OppfolgingStatus().setUnderOppfolging(false), "person ikke under oppfølging");
    }

    @Test
    @PactTestFor(pactMethod = "ikkeUnderOppfolgingPact")
    public void ikkeUnderOppfolging(MockServer mockServer) {
        SubjectHandler.withSubject(new Subject(FNR, EksternBruker, SSO_TOKEN), () -> {
            OppfolgingClient oppfolgingClient = buildClient(mockServer);
            assertThat(oppfolgingClient.underOppfolging(FNR)).isFalse();
        });
    }

    @Pact(provider = VEILARBOPPFOLGING, consumer = APPLICATION_NAME)
    public RequestResponsePact underOppfolgingPact(PactDslWithProvider builder) {
        return oppfolgingStatusPact(builder, new OppfolgingStatus().setUnderOppfolging(true), "person under oppfølging");
    }

    @Test
    @PactTestFor(pactMethod = "underOppfolgingPact")
    public void underOppfolging(MockServer mockServer) {
        SubjectHandler.withSubject(new Subject(FNR, EksternBruker, SSO_TOKEN), () -> {
            OppfolgingClient oppfolgingClient = buildClient(mockServer);
            assertThat(oppfolgingClient.underOppfolging(FNR)).isTrue();
        });
    }

    private RequestResponsePact oppfolgingStatusPact(PactDslWithProvider builder, OppfolgingStatus oppfolgingStatus, String state) {
        return builder
                .given(state)
                .uponReceiving("hent status på person under oppfølging")
                .path(VEILARBOPPFOLGING_API_PATH + "/oppfolging")
                .matchQuery(FNR_QUERY_PARAM, "\\d{11}", FNR)
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(dtoBody(oppfolgingStatus))
                .toPact();
    }

    private OppfolgingClient buildClient(MockServer mockServer) {
        String url = mockServer.getUrl() + VEILARBOPPFOLGING_API_PATH;
        return new OppfolgingClient(url);
    }

}
