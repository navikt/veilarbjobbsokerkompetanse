package no.nav.fo.veilarbjobbsokerkompetanse;

import lombok.SneakyThrows;
import no.nav.dialogarena.aktor.AktorConfig;
import no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig;
import no.nav.fo.veilarbjobbsokerkompetanse.db.DatabaseTestContext;
import no.nav.testconfig.ApiAppTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

import static java.lang.System.setProperty;
import static no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient.VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME;

public abstract class IntegrasjonsTest {

    private static AnnotationConfigApplicationContext annotationConfigApplicationContext;
    private static PlatformTransactionManager platformTransactionManager;
    private TransactionStatus transactionStatus;

    @BeforeAll
    @BeforeClass
    public static void setupContext() {
        ApiAppTest.setupTestContext();
        DatabaseTestContext.setupInMemoryContext();
        setupContext(
                ApplicationConfig.class
        );
    }

    @SneakyThrows
    private static void setupContext(Class<?>... classes) {
        DatabaseTestContext.setupContext(System.getProperty("database"));

        setProperty("no.nav.modig.security.sts.url", "111111");
        setProperty("no.nav.modig.security.systemuser.username", "username");
        setProperty("no.nav.modig.security.systemuser.password", "password");

        setProperty(AktorConfig.AKTOER_ENDPOINT_URL, "/");
        setProperty(VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME, "/");

        annotationConfigApplicationContext = new AnnotationConfigApplicationContext(classes);
        annotationConfigApplicationContext.start();
        platformTransactionManager = getBean(PlatformTransactionManager.class);

        MigrationUtils.createTables(getBean(DataSource.class));
    }

    @BeforeEach
    @Before
    public void injectAvhengigheter() {
        annotationConfigApplicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @BeforeEach
    @Before
    public void startTransaksjon() {
        transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
    }

    @AfterEach
    @After
    public void rollbackTransaksjon() {
        if (platformTransactionManager != null && transactionStatus != null) {
            platformTransactionManager.rollback(transactionStatus);
        }
    }

    protected static <T> T getBean(Class<T> requiredType) {
        return annotationConfigApplicationContext.getBean(requiredType);
    }

    @AfterAll
    @AfterClass
    public static void close() {
        if (annotationConfigApplicationContext != null) {
            annotationConfigApplicationContext.stop();
            annotationConfigApplicationContext.close();
            annotationConfigApplicationContext.destroy();
            annotationConfigApplicationContext = null;
        }
    }

}
