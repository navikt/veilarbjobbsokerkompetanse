package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.apiapp.selftest.Helsesjekk;
import no.nav.apiapp.selftest.HelsesjekkMetadata;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static no.nav.fo.veilarbjobbsokerkompetanse.db.DataSourceConfig.VEILARBJOBBSOKERKOMPETANSEDB_URL;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Component
public class DataSourceHelsesjekk implements Helsesjekk {

    @Inject
    private JdbcTemplate database;

    @Override
    public void helsesjekk() {
        database.queryForObject("SELECT 1 FROM DUAL", Long.class);
    }

    @Override
    public HelsesjekkMetadata getMetadata() {
        String databaseUri = getRequiredProperty(VEILARBJOBBSOKERKOMPETANSEDB_URL);
        return new HelsesjekkMetadata(
                "db",
            "Database: " + databaseUri,
            "Database for veilarbjobbsokerkompetanse",
            true
        );
    }
}
