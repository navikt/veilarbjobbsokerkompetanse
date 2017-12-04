package no.nav.fo.veilarbjobbsokerkompetanse;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
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

import static java.lang.System.getProperty;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Value("${veilarbjobbsokerkompetanseDB.url}")
    private String jdbcUrl;

    @Bean
    public static DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(getProperty("db.driverClass"));
        config.setJdbcUrl(getProperty("VEILARBJOBBSOKERKOMPETANSEDB_URL"));
        config.setUsername(getProperty("VEILARBJOBBSOKERKOMPETANSEDB_USERNAME"));
        config.setPassword(getProperty("VEILARBJOBBSOKERKOMPETANSEDB_PASSWORD"));
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
