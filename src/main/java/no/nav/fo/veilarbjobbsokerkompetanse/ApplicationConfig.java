package no.nav.fo.veilarbjobbsokerkompetanse;

import no.nav.apiapp.ApiApplication;
import no.nav.fo.veilarbjobbsokerkompetanse.db.DataSourceConfig;
import no.nav.fo.veilarbjobbsokerkompetanse.db.DataSourceHelsesjekk;
import no.nav.fo.veilarbjobbsokerkompetanse.db.JobbsokerKartleggingDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import static no.nav.apiapp.ApiApplication.Sone.FSS;

@Configuration
@ComponentScan("no.nav.fo.veilarbjobbsokerkompetanse.db")
@Import({
        DemoRessurs.class
})
public class ApplicationConfig implements ApiApplication {

    @Override
    public Sone getSone() {
        return FSS;
    }

    @Override
    public String getApplicationName() {
        return APPLICATION_NAME;
    }

    public static final String APPLICATION_NAME = "veilarbjobbsokerkompetanse";

    @Inject
    private DataSource dataSource;

    @Transactional
    @Override
    public void startup(ServletContext servletContext){
        MigrationUtils.createTables(dataSource);
    }


    @Bean
    public JobbsokerKartleggingDAO getJobbsokerKartleggingDAO(JdbcTemplate jdbcTemplate) {
        return new JobbsokerKartleggingDAO(jdbcTemplate);
    }
}