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

	/**
	 * @return the googleApiKey
	 */
	public String getGoogleApiKey() {
		return googleApiKey;
	}

	/**
	 * @param googleApiKey the googleApiKey to set
	 */
	public void setGoogleApiKey(String googleApiKey) {
		this.googleApiKey = googleApiKey;
	}
}