package no.nav.fo.veilarbjobbsokerkompetanse.provider;

import no.nav.apiapp.feil.Feil;
import no.nav.apiapp.security.veilarbabac.VeilarbAbacPepClient;
import no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import static no.nav.fo.veilarbjobbsokerkompetanse.provider.JobbsokerKartleggingRS.FNR_QUERY_PARAM;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobbsokerKartleggingRSTest {

    private static final String FNR = "12345678910";

    @Mock
    OppfolgingClient oppfolgingClient;

    @Mock
    Provider<HttpServletRequest> httpServletRequestProvider;

    @Mock
    VeilarbAbacPepClient pepClient;

    @InjectMocks
    JobbsokerKartleggingRS kartleggingService;

    @Before
    public void setup() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getParameter(FNR_QUERY_PARAM)).thenReturn(FNR);
        when(httpServletRequestProvider.get()).thenReturn(httpServletRequest);
    }

    @Test(expected = Feil.class)
    public void testCreateNarIkkeUnderOppfolging() {
        when(oppfolgingClient.underOppfolging(FNR)).thenReturn(false);
        kartleggingService.opprettBesvarelse(null);
    }

    @Test(expected = Feil.class)
    public void testFetchNarIkkeUnderOppfolging() {
        when(oppfolgingClient.underOppfolging(FNR)).thenReturn(false);
        kartleggingService.hentNyesteBesvarelseForAktor();
    }

}