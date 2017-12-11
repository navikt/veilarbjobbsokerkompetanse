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

        if (dbCredential.isPresent()) {
            setDataSourceProperties(dbCredential.get());
        } else {
            setInMemoryDataSourceProperties();
        }

    }

    public static void setupInMemoryContext() {
        setupContext(null);
    }

    private static void setDataSourceProperties(DbCredentials dbCredentials) {
        System.setProperty(
            VEILARBJOBBSOKERKOMPETANSEDB_URL, dbCredentials.url);
        System.setProperty(VEILARBJOBBSOKERKOMPETANSEDB_USERNAME, dbCredentials.getUsername());
        System.setProperty(VEILARBJOBBSOKERKOMPETANSEDB_PASSWORD, dbCredentials.getPassword());

    }

    private static void setInMemoryDataSourceProperties() {
        System.setProperty(
            VEILARBJOBBSOKERKOMPETANSEDB_URL,
            "jdbc:h2:mem:veilarbjobbsokerkompetanse;DB_CLOSE_DELAY=-1;MODE=Oracle");
        System.setProperty(VEILARBJOBBSOKERKOMPETANSEDB_USERNAME, "sa");
        System.setProperty(VEILARBJOBBSOKERKOMPETANSEDB_PASSWORD, "password");
    }
}