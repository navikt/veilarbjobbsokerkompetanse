package no.nav.fo.veilarbjobbsokerkompetanse.mock.config;

import no.nav.dialogarena.aktor.AktorConfig;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Conditional(Real.class)
@Configuration
@Import(AktorConfig.class)
public class RealConfiguration {

}
