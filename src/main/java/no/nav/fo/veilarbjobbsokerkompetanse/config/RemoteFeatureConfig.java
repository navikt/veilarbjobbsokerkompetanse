package no.nav.fo.veilarbjobbsokerkompetanse.config;


import no.nav.sbl.featuretoggle.remote.RemoteFeatureToggle;
import no.nav.sbl.featuretoggle.remote.RemoteFeatureToggleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.FEATURE_ENDPOINT_URL_PROPERTY_NAME;

@Configuration
public class RemoteFeatureConfig {

    @Value("${" + FEATURE_ENDPOINT_URL_PROPERTY_NAME + "}")
    private String remoteFeatureUrl;

    @Bean
    public RemoteFeatureToggleRepository remoteFeatureToggleRespository() {
        return new RemoteFeatureToggleRepository(remoteFeatureUrl);
    }

    @Bean
    public UnderOppfolgingFeature underOppfolgingFeature(RemoteFeatureToggleRepository repo) {
        return new UnderOppfolgingFeature(repo);
    }

    public static class UnderOppfolgingFeature extends RemoteFeatureToggle {
        public UnderOppfolgingFeature(RemoteFeatureToggleRepository repository) {
            super(repository, "jobbsokerkompetanse.sjekkunderoppfolging", false);
        }
    }

}
