package no.nav.fo.veilarbjobbsokerkompetanse;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.config.DataSourceConfig;
import no.nav.fo.veilarbjobbsokerkompetanse.db.DatabaseTestContext;
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

public abstract class DbIntegrasjonsTest {

    private static AnnotationConfigApplicationContext annotationConfigApplicationContext;
    private static PlatformTransactionManager platformTransactionManager;
    private TransactionStatus transactionStatus;
    private final boolean useTransactions;

    public DbIntegrasjonsTest(boolean useTransactions) {
        this.useTransactions = useTransactions;
    }

    public DbIntegrasjonsTest() {
        this(true);
    }

    @BeforeAll
    @BeforeClass
    public static void setupContext() {
        setupContext(DataSourceConfig.class);
    }

    @SneakyThrows
    private static void setupContext(Class<?>... classes) {
        DatabaseTestContext.setupContext(System.getProperty("database"));

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
        if (useTransactions) {
            transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        }
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
            annotationConfigApplicationContext = null;
        }
    }

}
