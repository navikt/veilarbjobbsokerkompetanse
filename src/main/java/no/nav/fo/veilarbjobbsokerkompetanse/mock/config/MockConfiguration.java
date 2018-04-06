package no.nav.fo.veilarbjobbsokerkompetanse.mock.config;

import lombok.extern.slf4j.Slf4j;
import no.nav.fo.veilarbjobbsokerkompetanse.mock.AktorServiceMock;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Conditional(Mock.class)
@Configuration
@Import({
        AktorServiceMock.class
})
public class MockConfiguration {

    public MockConfiguration() {
        log.warn("Running with mocks. YOU SHOULD NOT SEE THIS IN PRODUCTION");
    }
}
