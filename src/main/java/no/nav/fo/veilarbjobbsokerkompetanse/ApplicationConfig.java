package no.nav.fo.veilarbjobbsokerkompetanse;

import no.nav.apiapp.ApiApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import static no.nav.apiapp.ApiApplication.Sone.FSS;

@Configuration
@Import({
        DemoRessurs.class,
        DataSourceConfig.class
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

    @Override
    public void startup(ServletContext servletContext){
        MigrationUtils.createTables(dataSource);
    }


    @Bean
    public JobbsokerKartleggingDAO getJobbsokerKartleggingDAO(JdbcTemplate jdbcTemplate) {
        return new JobbsokerKartleggingDAO(jdbcTemplate);
    }
}