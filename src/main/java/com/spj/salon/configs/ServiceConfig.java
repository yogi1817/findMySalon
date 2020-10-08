package com.spj.salon.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 
 * @author Yogesh SHarma
 *
 */
@Component
@Data
public class ServiceConfig {

	@Value("${spring.datasource.url}")
	private String databaseUrl;
	
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
	
	@Value("${spring.mail.host}")
	private String mailHost;
	
	@Value("${spring.mail.port}")
	private int mailPort;
	
	@Value("${spj.services.authenticate}")
	private String authenticateService;
	
	@Value("${spj.application.id}")
	private String applicationId;
	
	@Value("${spj.application.password}")
	private String applicationPassword;
}