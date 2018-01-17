package no.nav.fo.veilarbjobbsokerkompetanse.provider.ws;

import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobbsokerkompetanseWSImplTest extends IntegrasjonsTest {

    private JobbsokerkompetanseWSImpl jobbsokerkompetanseWSImpl = mock(JobbsokerkompetanseWSImpl.class);

    @Test
    public void skal_opprette_og_hente_nyeste_jobbsokerKartlegging() {
        String aktorId = aktorId();
        JobbsokerKartlegging jobbsokerKartleggingOpprettet = opprettJobbsokerKartleggingMedAktorId(aktorId);

        JobbsokerKartlegging jobbsokerKartlegging = jobbsokerKartleggingDAO.hentNyesteJobbsokerKartlegging(aktorId);

        assertThat(jobbsokerKartlegging.getId(), equalTo(jobbsokerKartleggingOpprettet.getId()));
    }

    @Test
    public void aktorId_skal_returnere_null_hvis_fnr_er_null() {
        when(jobbsokerkompetanseWSImpl.fnr()).thenReturn(null);

        String aktorId = jobbsokerkompetanseWSImpl.aktorId();

        assertThat(aktorId, isEmptyOrNullString());
    }
}
