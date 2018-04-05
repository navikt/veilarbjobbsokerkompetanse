package no.nav.fo.veilarbjobbsokerkompetanse.mock.config;

import no.nav.fo.veilarbjobbsokerkompetanse.mock.AktorServiceMock;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Conditional(Mock.class)
@Configuration
@Import({
        AktorServiceMock.class
})
public class MockConfiguration {

}
