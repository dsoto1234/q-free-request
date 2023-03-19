package com.conduent.ibts.alpr.process.batch.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.core.dialect.AnsiDialect;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.conduent.ibts.alpr.process.batch.BatchProcessor;

import oracle.jdbc.pool.OracleDataSource;

/**
 * This class is created for create Spring Jdbc configuration.
 * 
 * 
 * @version 1.0
 * @since 15-01-2023
 */
@Configuration
public class BatchDBConfig extends AbstractJdbcConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(BatchDBConfig.class);
	
	@Autowired
	private Environment environment;
	
	public BatchDBConfig() {
		logger.info("<<<<< entering BatchDBConfig constructor() >>>>>");
	}
	

	@Override
	public Dialect jdbcDialect(NamedParameterJdbcOperations operations) {
		logger.info("<<<<< entering BatchDBConfig.jdbcDialect() >>>>>");
		return AnsiDialect.INSTANCE;
	}

	@Bean(name = "primaryDS")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource oracleDataSource() throws SQLException {
		logger.info("<<<<< entering BatchDBConfig.oracleDataSource() >>>>>");
		
		OracleDataSource dataSource = new OracleDataSource();
		dataSource.setURL(environment.getRequiredProperty("spring.datasource.url"));
		dataSource.setUser(environment.getRequiredProperty("spring.datasource.username"));
		dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));
		// dataSource.setImplicitCachingEnabled(true);
		// dataSource.setFastConnectionFailoverEnabled(true);
		return dataSource;
	}

	@Bean
	@Primary
	public JdbcTemplate jdbcTemplateOracle() throws SQLException {
		logger.info("<<<<< entering BatchDBConfig.jdbcTemplateOracle() >>>>>");
		
		return new JdbcTemplate(oracleDataSource());
	}

	@Bean
	@Primary
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() throws SQLException {
		logger.info("<<<<< entering BatchDBConfig.namedParameterJdbcTemplate() >>>>>");
		
		return new NamedParameterJdbcTemplate(oracleDataSource());
	}

}
