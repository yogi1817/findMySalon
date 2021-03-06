package com.spj.salon.interceptor;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 
 * @author Yogesh SHarma
 *
 */
@Component
@Data
public class UserContext {
	public static String CORRELATION_ID = "spj-correlation-id";
    public static String AUTH_TOKEN     = "Authorization";
    public static String USER_ID        = "spj-user-id";
    public static String ORG_ID         = "spj-org-id";
    public static String HOST			= "host";
	
    private String correlationId= "";
    private String authToken= "";
    private String userId = "";
    private String orgId = "";
    private String host = "";
}
