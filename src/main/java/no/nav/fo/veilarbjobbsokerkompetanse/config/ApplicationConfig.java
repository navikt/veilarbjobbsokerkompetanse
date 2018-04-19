package no.nav.fo.veilarbjobbsokerkompetanse.config;

import no.nav.apiapp.ApiApplication.NaisApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.fo.veilarbjobbsokerkompetanse.MigrationUtils;
import no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClient;
import no.nav.fo.veilarbjobbsokerkompetanse.client.OppfolgingClientHelseSjekk;
import no.nav.fo.veilarbjobbsokerkompetanse.db.BesvarelseDao;
import no.nav.fo.veilarbjobbsokerkompetanse.mock.config.MockConfiguration;
import no.nav.fo.veilarbjobbsokerkompetanse.mock.config.RealConfiguration;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.JobbsokerKartleggingRS;
import no.nav.fo.veilarbjobbsokerkompetanse.service.BesvarelseService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import static no.nav.apiapp.ApiApplication.Sone.FSS;
import static no.nav.sbl.util.EnvironmentUtils.getOptionalProperty;

@Configuration
@Import({
        RealConfiguration.class,
        MockConfiguration.class,
        DataSourceConfig.class,
        DataSourceHelsesjekk.class,
        BesvarelseService.class,
        JobbsokerKartleggingRS.class,
        BesvarelseDao.class,
        OppfolgingClient.class,
        OppfolgingClientHelseSjekk.class
})
public class ApplicationConfig implements NaisApiApplication {

    public static final String APPLICATION_NAME = "veilarbjobbsokerkompetanse";
    public static final String AKTOER_V2_ENDPOINTURL = "AKTOER_V2_ENDPOINTURL";
    public static final String RUN_WITH_MOCKS = "RUN_WITH_MOCKS";

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
                .sts()
                .issoLogin()
                .azureADB2CLogin()
        ;
    }

    public static boolean isMocksEnabled() {
        return Boolean.valueOf(getOptionalProperty(RUN_WITH_MOCKS).orElse("false"));
    }

}