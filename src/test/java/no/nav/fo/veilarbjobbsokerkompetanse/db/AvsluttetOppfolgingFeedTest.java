package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.feed.AvsluttetOppfolgingFeedService;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.AvsluttetOppfolgingFeedDto;
import org.junit.Test;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class AvsluttetOppfolgingFeedTest extends IntegrasjonsTest {

    private FeedMetaDataDao feedMetaDataDao = mock(FeedMetaDataDao.class);
    private KartleggingDao kartleggingDao = mock(KartleggingDao.class);

    @Test(expected=RuntimeException.class)
    public void skalIkkeOppdatereSisteIdHvisException() {
        doThrow(new RuntimeException("Mock exception")).when(kartleggingDao).anonymiserByAktorId(null, null);
        List<AvsluttetOppfolgingFeedDto> feedElements = asList(feedElement(null, new Date()));

        try {
            new AvsluttetOppfolgingFeedService(kartleggingDao, feedMetaDataDao).lesAvsluttetOppfolgingFeed(null, feedElements);
        } finally {
            verify(feedMetaDataDao, never()).oppdaterSisteLestTidspunkt(any(Date.class));
        }
    }

    private AvsluttetOppfolgingFeedDto feedElement(String aktorId, Date oppdatert) {
        return new AvsluttetOppfolgingFeedDto().setAktoerid(aktorId).setOppdatert(oppdatert);
    }

    @Test
    public void skalOppdatereSisteIdHvisOk() {
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 1000);
        List<AvsluttetOppfolgingFeedDto> feedElements = asList(feedElement("id1", date1), feedElement("id2", date2));
        new AvsluttetOppfolgingFeedService(kartleggingDao, feedMetaDataDao).lesAvsluttetOppfolgingFeed(null, feedElements);
        verify(feedMetaDataDao).oppdaterSisteLestTidspunkt(date2);
    }

    @Test
    public void skalAvslutteForAlleElementerIFeed() {
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 1000);
        List<AvsluttetOppfolgingFeedDto> feedElements = asList(feedElement("id1", date1), feedElement("id2", date2));
        new AvsluttetOppfolgingFeedService(kartleggingDao, feedMetaDataDao).lesAvsluttetOppfolgingFeed(null, feedElements);
        verify(kartleggingDao).anonymiserByAktorId("id1", null);
        verify(kartleggingDao).anonymiserByAktorId("id2", null);
        verifyNoMoreInteractions(kartleggingDao);
    }
}