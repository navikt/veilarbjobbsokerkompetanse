package no.nav.fo.veilarbjobbsokerkompetanse.service;

import no.nav.apiapp.feil.Feil;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbjobbsokerkompetanse.TestData;
import no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient;
import no.nav.fo.veilarbjobbsokerkompetanse.db.KartleggingDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KartleggingServiceTest {

    private static final String FNR = "12345678910";

    @Mock
    OppfolgingClient oppfolgingClient;

    @InjectMocks
    KartleggingService kartleggingService;

    @Test(expected = Feil.class)
    public void testCreateNarIkkeUnderOppfolging() {
        when(oppfolgingClient.underOppfolging(FNR)).thenReturn(false);
        kartleggingService.create(FNR, TestData.kartlegging());
    }

    @Test(expected = Feil.class)
    public void testFetchNarIkkeUnderOppfolging() {
        when(oppfolgingClient.underOppfolging(FNR)).thenReturn(false);
        kartleggingService.fetchMostRecentByFnr(FNR);
    }
}
