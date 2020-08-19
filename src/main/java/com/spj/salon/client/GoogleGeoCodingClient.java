package com.spj.salon.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Configuration
public class GoogleGeoCodingClient {

	@Autowired
	private EnvironmentConfig envConfig;

	@Autowired
	private Gson gson;

	private static final Logger logger = LogManager.getLogger(GoogleGeoCodingClient.class.getName());

	/**
	 * This service calls google geo-location api and get the location Commenting
	 * the code to find location on heroku as its working without any extra
	 * configuration Will try using QUOTAGUARD if it doesnot work on heroku
	 * 
	 * @param addessOrZip
	 * @return
	 * @throws IOException
	 */
	public GeocodingResult[] findGeocodingResult(String addessOrZip) throws IOException {

		String geoCodingUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + addessOrZip + "&key="
				+ envConfig.getGoogleApiKey();
		logger.info("geoCodingUrl --> {}", geoCodingUrl);

		URL url = new URL(geoCodingUrl);
		URLConnection conn = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		String responseBody = new BufferedReader(in).lines().collect(Collectors.joining("\n"));

		logger.debug("responseBody -->{}", responseBody);
		Response response = gson.fromJson(responseBody, Response.class);
		GeocodingResult[] results = response.results;

		return results;
	}
}
