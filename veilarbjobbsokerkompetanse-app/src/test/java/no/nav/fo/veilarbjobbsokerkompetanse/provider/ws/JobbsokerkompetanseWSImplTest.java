package no.nav.fo.veilarbjobbsokerkompetanse.provider.ws;

import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@Ignore
public class JobbsokerkompetanseWSImplTest extends IntegrasjonsTest {

    @Test
    public void skal_opprette_og_hente_nyeste_jobbsokerKartlegging() {
        String aktorId = aktorId();
        JobbsokerKartlegging jobbsokerKartleggingOpprettet = opprettJobbsokerKartleggingMedAktorId(aktorId);

        JobbsokerKartlegging jobbsokerKartlegging = jobbsokerKartleggingDAO.hentNyesteJobbsokerKartlegging(aktorId);

        assertThat(jobbsokerKartlegging.getId(), equalTo(jobbsokerKartleggingOpprettet.getId()));
    }
}
