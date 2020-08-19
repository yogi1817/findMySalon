package com.spj.salon.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.maps.GeocodingApi.Response;
import com.google.maps.model.GeocodingResult;
import com.spj.salon.config.EnvironmentConfig;

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
	
	@Autowired
	private StaticaProxyAuthenticator proxy;
	
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
				addessOrZip+"&key="+envConfig.getGoogleApiKey();
		logger.info("geoCodingUrl --> {}", geoCodingUrl);
				
		/*URLConnection conn = null;
		if(!"localhost".equals(UserContextHolder.getContext().getHost())) {
			logger.info("Inside else in  GoogleGeoCodingClient");
		
			URL proxyUrl = new URL(envConfig.getQuotoGuardShieldURL());
			logger.info("proxyUrl --> {}",proxyUrl);
			
	        String userInfo = proxyUrl.getUserInfo();
	        String user = userInfo.substring(0, userInfo.indexOf(':'));
	        String password = userInfo.substring(userInfo.indexOf(':') + 1);
	        logger.info("user --> {}",user);
	        
	        System.setProperty("http.proxyHost", proxyUrl.getHost());
	        System.setProperty("http.proxyPort", Integer.toString(proxyUrl.getPort()));
	        //System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
	        //System.setProperty("java.net.useSystemProxies", "true");
	        System.setProperty("https.proxyHost", proxyUrl.getHost());
			System.setProperty("https.proxyPort", Integer.toString(proxyUrl.getPort()));
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
		conn = url.openConnection();
		//conn.setConnectTimeout(30000);
		logger.info("google api connection stablished");*/
		
		 URL url = new URL(geoCodingUrl);
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestProperty("Proxy-Authorization", "Basic " + proxy.getEncodedAuth());
         Authenticator.setDefault(proxy.getAuth());
         conn.setRequestMethod("GET");
         
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String responseBody = new BufferedReader(in)
                .lines()
                .collect(Collectors.joining("\n"));
        
        in.close();
        System.clearProperty("http.proxyHost");
        
        logger.info("responseBody -->{}", responseBody);
		Response response = gson.fromJson(responseBody , Response.class);
		GeocodingResult[] results = response.results;
		
		return results;
	}
}
