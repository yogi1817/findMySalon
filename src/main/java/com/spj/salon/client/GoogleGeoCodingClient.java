package com.spj.salon.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.maps.GeocodingApi.Response;
import com.google.maps.model.GeocodingResult;
import com.spj.salon.config.EnvironmentConfig;
import com.spj.salon.utils.UserContextHolder;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Configuration
public class GoogleGeoCodingClient{

	@Autowired
	private EnvironmentConfig envConfig;
	
	@Autowired
	private Gson gson;
	
	private static final Logger logger = LogManager.getLogger(GoogleGeoCodingClient.class.getName());
	
	/**
	 * This service calls google geo-location api and get the location
	 * Commenting the code to find location on heroku as its working without any extra configuration
	 * Will try using QUOTAGUARD if it doesnot work on heroku
	 * @param addessOrZip
	 * @return
	 * @throws IOException
	 */
	public GeocodingResult[] findGeocodingResult(String addessOrZip) throws IOException {
		
		String geoCodingUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="+
				addessOrZip;
		if("localhost".equals(UserContextHolder.getContext().getHost())) {
			logger.info("Inside If in  GoogleGeoCodingClient");
			geoCodingUrl+="&key="+envConfig.getGoogleApiKey();
			logger.info("geoCodingUrl --> {}", geoCodingUrl);
		}else {
			logger.info("Inside else in  GoogleGeoCodingClient");
			geoCodingUrl+="&sensor=false";
			logger.info("geoCodingUrl --> {}", geoCodingUrl);
			URL proxyUrl = new URL(System.getenv("QUOTAGUARD_URL"));
			logger.info("proxyUrl --> {}",proxyUrl);
	        String userInfo = proxyUrl.getUserInfo();
	        String user = userInfo.substring(0, userInfo.indexOf(':'));
	        String password = userInfo.substring(userInfo.indexOf(':') + 1);

	        logger.info("user --> {}",user);
	        System.setProperty("http.proxyHost", proxyUrl.getHost());
	        System.setProperty("http.proxyPort", Integer.toString(proxyUrl.getPort()));
	        logger.info("proxyUrl.getHost() --> {}",proxyUrl.getHost());
	        logger.info("proxyUrl.getPort() --> {}",proxyUrl.getPort());
	        Authenticator.setDefault(new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(user, password.toCharArray());
	                }
	        });
	        logger.info("authentcation set");
		}
		URL url = new URL(geoCodingUrl);
		URLConnection conn = url.openConnection();
		logger.info("google api connection stablished");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String responseBody = new BufferedReader(in)
                .lines()
                .collect(Collectors.joining("\n"));
        logger.info("responseBody -->{}", responseBody);
		Response response = gson.fromJson(responseBody , Response.class);
		GeocodingResult[] results = response.results;
		
		return results;
	}
}
