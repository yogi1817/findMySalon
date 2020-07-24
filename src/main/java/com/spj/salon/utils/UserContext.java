package com.spj.salon.utils;

import org.springframework.stereotype.Component;

/**
 * 
 * @author Yogesh SHarma
 *
 */
@Component
public class UserContext {
	public static String CORRELATION_ID = "spj-correlation-id";
    public static String AUTH_TOKEN     = "Authorization";
    public static String USER_ID        = "spj-user-id";
    public static String ORG_ID         = "spj-org-id";
    public static String HOST			= "host";
	
    private String correlationId= new String();
    private String authToken= new String();
    private String userId = new String();
    private String orgId = new String();
    private String host = new String();
    
	/**
	 * @return the correlationId
	 */
	public String getCorrelationId() {
		return correlationId;
	}
	/**
	 * @param correlationId the correlationId to set
	 */
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	/**
	 * @return the authToken
	 */
	public String getAuthToken() {
		return authToken;
	}
	/**
	 * @param authToken the authToken to set
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	} 
}
