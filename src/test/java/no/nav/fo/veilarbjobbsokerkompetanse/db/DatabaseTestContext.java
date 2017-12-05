package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.val;
import no.nav.dialogarena.config.fasit.DbCredentials;
import no.nav.dialogarena.config.fasit.FasitUtils;
import no.nav.dialogarena.config.fasit.TestEnvironment;

import java.util.Optional;

import static no.nav.fo.veilarbjobbsokerkompetanse.ApplicationConfig.APPLICATION_NAME;
import static no.nav.fo.veilarbjobbsokerkompetanse.DataSourceConfig.*;

public class DatabaseTestContext {

    public static void setupContext(String miljo) {
        val dbCredential = Optional.ofNullable(miljo)
                .map(TestEnvironment::valueOf)
                .map(testEnvironment -> FasitUtils.getDbCredentials(testEnvironment, APPLICATION_NAME));

        if (dbCredential.isPresent()){
            setDataSourceProperties(dbCredential.get());
        } else {
            setInMemoryDataSourceProperties();
        }

    }

    private static void setDataSourceProperties(DbCredentials dbCredentials) {
        System.setProperty(
            VEILARBJOBBSOKERKOMPETANSEDB_URL, dbCredentials.url);
        System.setProperty(VEILARBJOBBSOKERKOMPETANSEDB_USERNAME, dbCredentials.getUsername());
        System.setProperty(VEILARBJOBBSOKERKOMPETANSEDB_PASSWORD, dbCredentials.getPassword());
        System.setProperty(DB_DRIVER_CLASS, "oracle.jdbc.driver.OracleDriver");

    }

    private static void setInMemoryDataSourceProperties() {
        System.setProperty(
                VEILARBJOBBSOKERKOMPETANSEDB_URL,
                "jdbc:h2:mem:veilarbjobbsokerkompetanse;DB_CLOSE_DELAY=-1;MODE=Oracle");
        System.setProperty(VEILARBJOBBSOKERKOMPETANSEDB_USERNAME, "sa");
        System.setProperty(VEILARBJOBBSOKERKOMPETANSEDB_PASSWORD, "password");
        System.setProperty(DB_DRIVER_CLASS, "org.h2.Driver");
    }

//    public static SingleConnectionDataSource buildDataSourceFor(String miljo) {
//        return of(miljo)
//                .map(TestEnvironment::valueOf)
//                .map(testEnvironment -> FasitUtils.getDbCredentials(testEnvironment, APPLICATION_NAME))
//                .map(DatabaseTestContext::build)
//                .orElseGet(DatabaseTestContext::buildDataSource);
//    }

//    public static SingleConnectionDataSource buildDataSource() {
//        return doBuild(new DbCredentials()
//                        .setUrl(TestDriver.getURL())
//                        .setUsername("sa")
//                        .setPassword(""),
//                true
//        );
//    }
//
//    public static SingleConnectionDataSource build(DbCredentials dbCredentials) {
//        return doBuild(dbCredentials,false);
//    }
//
//    private static SingleConnectionDataSource doBuild(DbCredentials dbCredentials, boolean migrate) {
//        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
//        dataSource.setSuppressClose(true);
//        dataSource.setUrl(dbCredentials.url);
//        dataSource.setUsername(dbCredentials.username);
//        dataSource.setPassword(dbCredentials.password);
//        if (migrate){
//            createTables(dataSource);
//        }
//        return dataSource;
//    }
//
//    private static void createTables(SingleConnectionDataSource singleConnectionDataSource) {
//        Flyway flyway = new Flyway();
//        flyway.setDataSource(singleConnectionDataSource);
//        int migrate = flyway.migrate();
////        assertThat(migrate, greaterThan(0));
//    }
}