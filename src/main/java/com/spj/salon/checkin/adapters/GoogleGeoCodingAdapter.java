package com.spj.salon.checkin.adapters;

import com.google.gson.Gson;
import com.google.maps.GeocodingApi.Response;
import com.google.maps.model.GeocodingResult;
import com.spj.salon.checkin.ports.out.GeoCoding;
import com.spj.salon.configs.EnvironmentConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.stream.Collectors;

/**
 * @author Yogesh Sharma
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class GoogleGeoCodingAdapter implements GeoCoding {

    private final EnvironmentConfig envConfig;

    private final Gson gson;

    /**
     * This method calls geolocation using proxy qgpass
     *
     * @param addessOrZip
     * @return
     * @throws IOException
     */
    public GeocodingResult[] findGeocodingResult(String addessOrZip) throws IOException {
        URL proxyUrl = new URL(envConfig.getQuotoGuardShieldURL());
        String userInfo = proxyUrl.getUserInfo();
        String user = userInfo.substring(0, userInfo.indexOf(':'));
        String password = userInfo.substring(userInfo.indexOf(':') + 1);

        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "false");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "false");

        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password.toCharArray());
            }
        });

        Proxy webProxy
                = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl.getHost(), proxyUrl.getPort()));

        String geoCodingUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + addessOrZip + "&key="
                + envConfig.getGoogleApiKey();
        String responseBody = null;

        try {
            URL url = new URL(geoCodingUrl);
            HttpURLConnection webProxyConnection
                    = (HttpURLConnection) url.openConnection(webProxy);
            log.info("Open Connection");
            BufferedReader in = new BufferedReader(new InputStreamReader(webProxyConnection.getInputStream()));

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
