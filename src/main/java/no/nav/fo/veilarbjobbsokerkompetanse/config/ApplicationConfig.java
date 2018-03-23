package no.nav.fo.veilarbjobbsokerkompetanse.config;

import no.nav.apiapp.ApiApplication.NaisApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.dialogarena.aktor.AktorConfig;
import no.nav.fo.veilarbjobbsokerkompetanse.MigrationUtils;
import no.nav.fo.veilarbjobbsokerkompetanse.db.BesvarelseDao;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.JobbsokerKartleggingRS;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import static no.nav.apiapp.ApiApplication.Sone.FSS;

@Configuration
@Import({
        AktorConfig.class,
        DataSourceConfig.class,
        DataSourceHelsesjekk.class,
        JobbsokerKartleggingRS.class,
        BesvarelseDao.class,
})
public class ApplicationConfig implements NaisApiApplication {

    public static final String APPLICATION_NAME = "veilarbjobbsokerkompetanse";
    public static final String AKTOER_V2_ENDPOINTURL = "AKTOER_V2_ENDPOINTURL";

    @Inject
    private DataSource dataSource;

    @Override
    public Sone getSone() {
        return FSS;
    }

    @Override
    public String getApplicationName() {
        return APPLICATION_NAME;
    }

    @Transactional
    @Override
    public void startup(ServletContext servletContext) {
        MigrationUtils.createTables(dataSource);
    }

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {
        apiAppConfigurator
                .samlLogin()
                .sts();
    }

}