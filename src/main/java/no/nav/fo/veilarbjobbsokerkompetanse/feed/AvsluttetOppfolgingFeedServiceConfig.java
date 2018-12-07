package no.nav.fo.veilarbjobbsokerkompetanse.feed;

// import no.nav.fo.veilarbjobbsokerkompetanse.config.FeatureToggle;
import no.nav.fo.veilarbjobbsokerkompetanse.db.FeedMetaDataDao;
import no.nav.fo.veilarbjobbsokerkompetanse.db.KartleggingDao;
import no.nav.sbl.jdbc.Database;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AvsluttetOppfolgingFeedServiceConfig {
    @Bean
    public FeedMetaDataDao feedMetaDataDao(Database db) {
        return new FeedMetaDataDao(db);
    }

    @Bean
    public AvsluttetOppfolgingFeedService oppfolgingFeedService(
        KartleggingDao kartleggingDao,
        FeedMetaDataDao feedMetaDataDao
    ) {
        return new AvsluttetOppfolgingFeedService(kartleggingDao, feedMetaDataDao);
    }
}
