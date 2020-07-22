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

	@Value("${spj.variables.googleapikey}")
	private String googleApiKey;

	@Value("${spj.jwt.signingkey}")
	private String jwtSigningKey;

	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	@Value("${spj.twiliootp.sid}")
	private String twilioSid;
	
	@Value("${spj.twiliootp.authtoken}")
	private String twilioAuthToken;
	
	/**
	 * @return the googleApiKey
	 */
	public String getGoogleApiKey() {
		return googleApiKey;
	}

	/**
	 * @return the jwtSigningKey
	 */
	public String getJwtSigningKey() {
		return jwtSigningKey;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the twilioSid
	 */
	public String getTwilioSid() {
		return twilioSid;
	}

	/**
	 * @return the twilioAuthToken
	 */
	public String getTwilioAuthToken() {
		return twilioAuthToken;
	}
}