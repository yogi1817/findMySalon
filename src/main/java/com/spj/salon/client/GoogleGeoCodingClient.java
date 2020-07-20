package com.spj.salon.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.maps.GeocodingApi.Response;
import com.google.maps.model.GeocodingResult;
import com.spj.salon.config.ServiceConfig;
import com.spj.salon.utils.UserContextHolder;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Configuration
public class GoogleGeoCodingClient{

	@Autowired
	private ServiceConfig serviceConfig;
	
	@Autowired
	private Gson gson;
	
	private static final Logger logger = LoggerFactory.getLogger(GoogleGeoCodingClient.class.getName());
	
	public GeocodingResult[] findGeocodingResult(String addessOrZip) throws IOException {
		
		String geoCodingUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="+
				addessOrZip;
		if("localhost".equals(UserContextHolder.getContext().getHost())) {
			logger.debug("Inside If in  GoogleGeoCodingClient");
			geoCodingUrl+="&key="+serviceConfig.getGoogleApiKey();
			logger.debug("geoCodingUrl --> {}", geoCodingUrl);
		}else {
			logger.debug("Inside else in  GoogleGeoCodingClient");
			geoCodingUrl+="&sensor=false";
			logger.debug("geoCodingUrl --> {}", geoCodingUrl);
			URL proxyUrl = new URL(System.getenv("QUOTAGUARD_URL"));
			logger.debug("proxyUrl --> {}",proxyUrl);
	        String userInfo = proxyUrl.getUserInfo();
	        String user = userInfo.substring(0, userInfo.indexOf(':'));
	        String password = userInfo.substring(userInfo.indexOf(':') + 1);

	        logger.debug("user --> {}",user);
	        System.setProperty("http.proxyHost", proxyUrl.getHost());
	        System.setProperty("http.proxyPort", Integer.toString(proxyUrl.getPort()));
	        logger.debug("proxyUrl.getHost() --> {}",proxyUrl.getHost());
	        logger.debug("proxyUrl.getPort() --> {}",proxyUrl.getPort());
	        Authenticator.setDefault(new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(user, password.toCharArray());
	                }
	        });
		}
		
		URL url = new URL(geoCodingUrl);
		URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String responseBody = new BufferedReader(in)
                .lines()
                .collect(Collectors.joining("\n"));
        logger.debug("responseBody -->{}", responseBody);
		Response response = gson.fromJson(responseBody , Response.class);
		GeocodingResult[] results = response.results;
		
		return results;
	}
}
