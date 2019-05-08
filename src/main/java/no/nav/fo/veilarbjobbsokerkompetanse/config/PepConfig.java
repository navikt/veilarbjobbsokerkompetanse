package no.nav.fo.veilarbjobbsokerkompetanse.config;

import no.nav.apiapp.security.veilarbabac.VeilarbAbacPepClient;
import no.nav.brukerdialog.security.oidc.SystemUserTokenProvider;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import no.nav.sbl.dialogarena.common.abac.pep.context.AbacContext;
import no.nav.sbl.featuretoggle.unleash.UnleashService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Inject;

import static no.nav.sbl.dialogarena.common.abac.pep.domain.ResourceType.Person;

@Configuration
@Import({AbacContext.class})
public class PepConfig {

    private SystemUserTokenProvider systemUserTokenProvider = new SystemUserTokenProvider();

    @Inject
    UnleashService unleashService;

    @Bean
    public VeilarbAbacPepClient pepClient(Pep pep) {

        return VeilarbAbacPepClient.ny()
                .medPep(pep)
                .medResourceTypePerson()
                .medSystemUserTokenProvider(()->systemUserTokenProvider.getToken())
                .brukAktoerId(()->unleashService.isEnabled("veilarbjobbsokerkompetanse.veilarbabac.aktor"))
                .sammenlikneTilgang(()->unleashService.isEnabled("veilarbjobbsokerkompetanse.veilarbabac.sammenlikn"))
                .foretrekkVeilarbAbacResultat(()->unleashService.isEnabled("veilarbjobbsokerkompetanse.veilarbabac.foretrekk_veilarbabac"))
                .bygg();
    }

}
