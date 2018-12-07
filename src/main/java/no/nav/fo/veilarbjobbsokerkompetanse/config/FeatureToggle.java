package no.nav.fo.veilarbjobbsokerkompetanse.config;

import no.nav.sbl.featuretoggle.unleash.UnleashService;

import javax.inject.Inject;

public class FeatureToggle {

    private UnleashService unleashService;

    @Inject
    public FeatureToggle(UnleashService unleashService) {
        this.unleashService = unleashService;
    }
}