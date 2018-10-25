package no.nav.fo.veilarbjobbsokerkompetanse.feed;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AvsluttetOppfolgingFeedServiceConfig.class,
        AvsluttetOppfolgingConsumerConfig.class
})
public class FeedConfig {
}
