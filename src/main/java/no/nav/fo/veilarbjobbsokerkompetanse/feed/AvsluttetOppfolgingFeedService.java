package no.nav.fo.veilarbjobbsokerkompetanse.feed;
import lombok.extern.slf4j.Slf4j;
import no.nav.fo.veilarbjobbsokerkompetanse.db.FeedMetaDataDao;
import no.nav.fo.veilarbjobbsokerkompetanse.db.KartleggingDao;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.AvsluttetOppfolgingFeedDto;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class AvsluttetOppfolgingFeedService {

    private KartleggingDao kartleggingDao;
    private FeedMetaDataDao feedMetaDataDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(KartleggingDao.class);

    public AvsluttetOppfolgingFeedService(KartleggingDao kartleggingDao, FeedMetaDataDao feedMetaDataDao) {
        this.kartleggingDao = kartleggingDao;
        this.feedMetaDataDao = feedMetaDataDao;
    }

    String sisteEndring() {
        Date sisteEndring = feedMetaDataDao.hentSisteLestTidspunkt();
        return ZonedDateTime.ofInstant(sisteEndring.toInstant(), ZoneId.systemDefault()).toString();
    }

    public void lesAvsluttetOppfolgingFeed(String lastEntry, List<AvsluttetOppfolgingFeedDto> feedElements) {
        LOGGER.info("lest {} aktører fra avsluttetoppfolging-feed", feedElements.size());

        Date lastSuccessfulId = null;
        int successfulIdCount = 0;

        for (AvsluttetOppfolgingFeedDto element : feedElements) {
            kartleggingDao.anonymiserByAktorId(element.getAktoerid(), element.getSluttdato());
            lastSuccessfulId = element.getOppdatert();
            successfulIdCount ++;
        }

        // Håndterer ikke exceptions her. Dersom en exception oppstår i løkkeprosesseringen over, vil
        // vi altså IKKE få oppdatert siste id. Dermed vil vi lese feeden på nytt fra siste kjente id og potensielt
        // prosessere noen elementer flere ganger. Dette skal gå bra, siden koden som setter dialoger til historisk
        // er idempotent
        if(lastSuccessfulId != null) {
            feedMetaDataDao.oppdaterSisteLestTidspunkt(lastSuccessfulId);
            LOGGER.info("anonymisering av {} aktører fullført", successfulIdCount);
        }
        LOGGER.error("anonymisering av aktører feilet");
    }
}