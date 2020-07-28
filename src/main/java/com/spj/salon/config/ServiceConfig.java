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
	
	@Value("${spring.mail.host}")
	private String mailHost;
	
	@Value("${spring.mail.port}")
	private int mailPort;

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

	/**
	 * @return the mailHost
	 */
	public String getMailHost() {
		return mailHost;
	}

	/**
	 * @return the mailPort
	 */
	public int getMailPort() {
		return mailPort;
	}
}