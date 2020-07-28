package com.spj.salon;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spj.salon.config.EnvironmentConfig;
import com.spj.salon.config.ServiceConfig;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Configuration
public class DataSourceConfig {

	private static final Logger logger = LogManager.getLogger(DataSourceConfig.class);
	
	@Autowired
	private EnvironmentConfig envConfig;
	
	@Autowired
	private ServiceConfig serviceConfig;
	
	@Bean
	public DataSource getDataDource() {
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.username(envConfig.getDatasourceUsername());
		dataSourceBuilder.password(envConfig.getDatasourcePassword());
		dataSourceBuilder.url(serviceConfig.getDatabaseUrl());
		dataSourceBuilder.driverClassName(serviceConfig.getDriverClassName());
		
		logger.debug("Connection stablished");
		return dataSourceBuilder.build();
	}
}
