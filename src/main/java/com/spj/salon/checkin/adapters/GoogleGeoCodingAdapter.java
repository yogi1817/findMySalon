package com.spj.salon.checkin.adapters;

import com.google.gson.Gson;
import com.google.maps.GeocodingApi.Response;
import com.google.maps.model.GeocodingResult;
import com.spj.salon.checkin.ports.out.GeoCoding;
import com.spj.salon.configs.EnvironmentConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

/**
 * @author Yogesh Sharma
 */
@Configuration
@Slf4j
public class GoogleGeoCodingAdapter implements GeoCoding {

    @Autowired
    private EnvironmentConfig envConfig;

    @Autowired
    private Gson gson;

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
		URL proxyUrl = new URL(System.getenv("QUOTAGUARDSHIELD_URL"));
        String userInfo = proxyUrl.getUserInfo();
        String user = userInfo.substring(0, userInfo.indexOf(':'));
        String password = userInfo.substring(userInfo.indexOf(':') + 1);

        log.info("user --> {}", user);
        log.info("proxyUrl.getHost() --> {}", proxyUrl.getHost());
        log.info("proxyUrl.getPort() --> {}", Integer.toString(proxyUrl.getPort()));

        URLConnection conn = null;
        System.setProperty("http.proxyHost", proxyUrl.getHost());
        System.setProperty("http.proxyPort", Integer.toString(proxyUrl.getPort()));

        Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password.toCharArray());
                }
        });

        String geoCodingUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + addessOrZip + "&key="
                + envConfig.getGoogleApiKey();
        log.info("geoCodingUrl --> {}", geoCodingUrl);
        String responseBody = null;

        try {
            URL url = new URL(geoCodingUrl);
            conn = url.openConnection();
            log.info("Open Connection");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            log.info("Created Buffer Reader");
            responseBody = new BufferedReader(in).lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        log.info("responseBody -->{}", responseBody);
        Response response = gson.fromJson(responseBody, Response.class);

        return response.results;
    }
}
