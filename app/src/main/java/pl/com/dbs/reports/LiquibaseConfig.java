package pl.com.dbs.reports;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Liquibase configuration.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Configuration
public class LiquibaseConfig {
	@Value("${liquibase.contexts}")
	private String contexts;

	@Value("${liquibase.changelog}")
	private String changelog;

	@Value("${liquibase.shouldRun}")
	private boolean shouldRun;

	@Autowired
	private DataSource dataSource;

	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog(changelog);
		liquibase.setContexts(contexts);
		liquibase.setDataSource(dataSource);
		liquibase.setShouldRun(shouldRun);
		return liquibase;
	}

}
