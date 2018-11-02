package no.nav.fo.veilarbjobbsokerkompetanse.feed;
import lombok.extern.slf4j.Slf4j;
import no.nav.fo.veilarbjobbsokerkompetanse.Metrikker;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(AvsluttetOppfolgingFeedService.class);

    public AvsluttetOppfolgingFeedService(KartleggingDao kartleggingDao, FeedMetaDataDao feedMetaDataDao) {
        this.kartleggingDao = kartleggingDao;
        this.feedMetaDataDao = feedMetaDataDao;
    }

    String sisteEndring() {
        Date sisteEndring = feedMetaDataDao.hentSisteLestTidspunkt();
        return ZonedDateTime.ofInstant(sisteEndring.toInstant(), ZoneId.systemDefault()).toString();
    }

    public void lesAvsluttetOppfolgingFeed(String lastEntry, List<AvsluttetOppfolgingFeedDto> feedElements) {
        LOGGER.info("Anonymisering: {} nye aktørIDer fra avsluttetoppfolging-feed", feedElements.size());

        Date lastSuccessfulId = null;
        int raderAnonymisert = 0;

        for (AvsluttetOppfolgingFeedDto element : feedElements) {
            int rader = kartleggingDao.anonymiserByAktorId(element.getAktoerid(), element.getSluttdato());
            lastSuccessfulId = element.getOppdatert();
            raderAnonymisert += rader;
            LOGGER.info("Anonymisering for bruker: oppdatert = {}, rader endret = {}", element.getOppdatert(), rader);
        }

        // Håndterer ikke exceptions her. Dersom en exception oppstår i løkkeprosesseringen over, vil
        // vi altså IKKE få oppdatert siste id. Dermed vil vi lese feeden på nytt fra siste kjente id og potensielt
        // prosessere noen elementer flere ganger. Dette skal gå bra, siden koden som setter dialoger til historisk
        // er idempotent
        if(lastSuccessfulId != null) {
            LOGGER.info("Anonymisering: lastSuccessfulId = {}", lastSuccessfulId);
            feedMetaDataDao.oppdaterSisteLestTidspunkt(lastSuccessfulId);
        }

        LOGGER.info("Anonymisering: {} kartlegginger anonymisert for {} aktørIDer", raderAnonymisert, feedElements.size());
        Metrikker.anonymiseringAvKartleggingerMetrikk(feedElements.size(), raderAnonymisert);

        LOGGER.info("Anonymisering avsluttet!");
    }
}