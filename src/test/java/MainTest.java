import no.nav.fo.veilarbjobbsokerkompetanse.TestContext;
import no.nav.fo.veilarbjobbsokerkompetanse.db.DatabaseTestContext;
import no.nav.testconfig.ApiAppTest;

import static java.lang.System.getProperty;

public class MainTest {

    private static final String PORT = "8800";

    public static void main(String[] args) throws Exception {
        ApiAppTest.setupTestContext();
        DatabaseTestContext.setupContext(getProperty("database"));
        TestContext.setup();

        Main.main(PORT);
    }

}
