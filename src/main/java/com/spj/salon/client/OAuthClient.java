package com.spj.salon.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.spj.salon.config.ServiceConfig;
import com.spj.salon.user.model.User;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Configuration
public class OAuthClient{

	@Autowired
	private Gson gson;
	
	@Autowired
	private ServiceConfig serviceConfig;
	
	private static final Logger logger = LogManager.getLogger(OAuthClient.class.getName());
	
	/**
	 * This method calls the oauth service with login id and password and
	 * generates the jwt token. 
	 * This is needed because the UI(angular) was not able to call the OAUTh service provided by spring security
	 * @param user
	 * @param clientHost
	 * @return
	 */
	public User getJwtToken(User user, String clientHost) throws OAuth2Exception{
		String loginId = user.getLoginId()==null?user.getEmail():user.getLoginId();
		user.setJwtToken(getJwtToken(loginId, user.getPassword(), clientHost));
		user.setPassword("");
		return user;
	}
	
	public String getJwtToken(String loginId, String password, String clientHost) throws OAuth2Exception{
		String authenticateClient = clientHost + serviceConfig.getAuthenticateService();

		String authString = serviceConfig.getApplicationId() + ":" + serviceConfig.getApplicationPassword();
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		
		OkHttpClient client = new OkHttpClient().newBuilder()
				  .build();
				
		RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
				  .addFormDataPart("grant_type", "password")
				  .addFormDataPart("scope", "webclient")
				  .addFormDataPart("username", loginId)
				  .addFormDataPart("password", password)
				  .build();
		
		Request request = new Request.Builder()
				  .url(authenticateClient)
				  .method("POST", body)
				  .addHeader("Authorization", "Basic "+authStringEnc)
				  .build();
		
		Map<String, String> token = null;
		Type empMapType = new TypeToken<Map<String, String>>(){
					private static final long serialVersionUID = 1L;}
			.getType();
		try {
			Response response = client.newCall(request).execute();
	        BufferedReader in = new BufferedReader(new InputStreamReader(response.body().byteStream()));

	        String responseBody = new BufferedReader(in)
	                .lines()
	                .collect(Collectors.joining("\n"));
	        logger.debug("responseBody -->{}", responseBody);
			
			token = gson.fromJson(responseBody, empMapType);
			
		} catch (Exception e) {
			logger.error("Error calling auth service "+e.getMessage());
			throw new ServiceException("Failed to call oauth service");
		}
		
		logger.info("response "+ token);
		
		if(token.get("access_token")==null)
			throw new OAuth2Exception(token.toString());
		
		return token.get("access_token");
	}
}
