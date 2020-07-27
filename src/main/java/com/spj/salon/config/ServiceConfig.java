package com.spj.salon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Yogesh SHarma
 *
 */
@Component
public class ServiceConfig {

	@Value("${spring.datasource.url}")
	private String databaseUrl;
	
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;

	/**
	 * @return the databaseUrl
	 */
	public String getDatabaseUrl() {
		return databaseUrl;
	}

	/**
	 * @return the driverClassName
	 */
	public String getDriverClassName() {
		return driverClassName;
	}
}