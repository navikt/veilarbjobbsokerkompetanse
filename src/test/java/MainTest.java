import no.nav.fo.veilarbjobbsokerkompetanse.IntegrasjonsTest;
import no.nav.fo.veilarbjobbsokerkompetanse.db.DatabaseTestContext;

public class MainTest {

    public static final String TEST_PORT = "8800";

    public static void main(String[] args) throws Exception {
        DatabaseTestContext.setupContext(System.getProperty("database"));
        Main.main(TEST_PORT);
    }

}
