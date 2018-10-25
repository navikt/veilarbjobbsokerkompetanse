package no.nav.fo.veilarbjobbsokerkompetanse.feed;

import net.javacrumbs.shedlock.core.LockProvider;
import no.nav.brukerdialog.security.oidc.OidcFeedOutInterceptor;
import no.nav.fo.feed.consumer.FeedConsumer;
import no.nav.fo.feed.consumer.FeedConsumerConfig;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.AvsluttetOppfolgingFeedDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Collections;
import net.javacrumbs.shedlock.provider.jdbc.JdbcLockProvider;

import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Configuration
public class AvsluttetOppfolgingConsumerConfig {

    public static final String VEILARBOPPFOLGINGAPI_URL_PROPERTY = "VEILARBOPPFOLGINGAPI_URL";
    private static final String AVSLUTTETOPPFOLGING_FEED_NAME = "avsluttetoppfolging";
    public static final String POLLINGRATE = "avsluttoppfolging.feed.consumer.pollingrate";
    private final String host;
    private final String polling;

    @Inject
    private DataSource dataSource;

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcLockProvider(dataSource);
    }

    public AvsluttetOppfolgingConsumerConfig() {
        host = getRequiredProperty(VEILARBOPPFOLGINGAPI_URL_PROPERTY);
        polling = getRequiredProperty(POLLINGRATE);
    }

    @Bean
    public FeedConsumer<AvsluttetOppfolgingFeedDto> oppfolgingFeedConsumer(AvsluttetOppfolgingFeedService service) {
        FeedConsumerConfig<AvsluttetOppfolgingFeedDto> config = new FeedConsumerConfig<>(
                new FeedConsumerConfig.BaseConfig<>(
                        AvsluttetOppfolgingFeedDto.class,
                        service::sisteEndring,
                        host,
                        AVSLUTTETOPPFOLGING_FEED_NAME
                ),
                new FeedConsumerConfig.CronPollingConfig(polling)
        )
                .lockProvider(lockProvider(dataSource), 10000)
                .callback(service::lesAvsluttetOppfolgingFeed)
                .interceptors(Collections.singletonList(new OidcFeedOutInterceptor()));

        return new FeedConsumer<>(config);
    }
}