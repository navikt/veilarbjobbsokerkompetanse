package no.nav.fo.veilarbjobbsokerkompetanse;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    public static final String VEILARBJOBBSOKERKOMPETANSEDB_URL = "VEILARBJOBBSOKERKOMPETANSEDB_URL";
    public static final String VEILARBJOBBSOKERKOMPETANSEDB_USERNAME = "VEILARBJOBBSOKERKOMPETANSEDB_USERNAME";
    public static final String VEILARBJOBBSOKERKOMPETANSEDB_PASSWORD = "VEILARBJOBBSOKERKOMPETANSEDB_PASSWORD";
    public static final String DB_DRIVER_CLASS = "db.driverClass";

    @Bean
    public static DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(getRequiredProperty(DB_DRIVER_CLASS));
        config.setJdbcUrl(getRequiredProperty(VEILARBJOBBSOKERKOMPETANSEDB_URL));
        config.setUsername(getRequiredProperty(VEILARBJOBBSOKERKOMPETANSEDB_USERNAME));
        config.setPassword(getRequiredProperty(VEILARBJOBBSOKERKOMPETANSEDB_PASSWORD));
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);

        return new HikariDataSource(config);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(DataSource ds) throws NamingException {
        return new DataSourceTransactionManager(ds);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) throws NamingException, SQLException, IOException {
        return new JdbcTemplate(dataSource);
    }

}
