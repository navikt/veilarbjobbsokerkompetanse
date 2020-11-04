package no.nav.fo.veilarbjobbsokerkompetanse.config;

import no.nav.sbl.featuretoggle.unleash.UnleashService;
import no.nav.sbl.featuretoggle.unleash.UnleashServiceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeatureToggleConfig {

    private final static String UNLEASH_API_URL = "https://unleash.nais.io/api/";

    @Bean
    public UnleashService unleashService() {
        UnleashServiceConfig config = UnleashServiceConfig.builder()
                .applicationName("veilarbjobbsokerkompetanse")
                .unleashApiUrl(UNLEASH_API_URL)
                .build();

        return new UnleashService(config);
    }

    @Bean
    public FeatureToggle feature() {
        return new FeatureToggle(unleashService());
    }
}
