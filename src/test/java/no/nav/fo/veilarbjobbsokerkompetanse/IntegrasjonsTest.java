package no.nav.fo.veilarbjobbsokerkompetanse;

import no.nav.fo.veilarbjobbsokerkompetanse.db.DatabaseTestContext;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;

public abstract class IntegrasjonsTest extends AbstractIntegrasjonsTest {

    @BeforeAll
    @BeforeClass
    public static void setupContext() {
        setupContext(
                ApplicationConfig.class
        );
    }

}
