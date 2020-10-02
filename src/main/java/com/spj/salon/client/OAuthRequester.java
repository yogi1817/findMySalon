package com.spj.salon.client;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.spj.salon.barber.ports.out.OAuthClient;
import com.spj.salon.config.ServiceConfig;
import com.spj.salon.customer.model.User;
import com.spj.salon.openapi.resources.AuthenticationRequest;
import com.spj.salon.openapi.resources.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yogesh Sharma
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class OAuthRequester implements OAuthClient {

    private final Gson gson;
    private final ServiceConfig serviceConfig;

    /**
     * This method calls the oauth service with login id and password and
     * generates the jwt token.
     * This is needed because the UI(angular) was not able to call the OAUTh service provided by spring security
     *
     * @param String
     * @param clientHost
     * @return
     */
    @Override
    public String getJwtToken(String email, String password, String clientHost) throws OAuth2Exception {
        String authenticateClient = clientHost + serviceConfig.getAuthenticateService();

        String authString = serviceConfig.getApplicationId() + ":" + serviceConfig.getApplicationPassword();
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "password")
                .addFormDataPart("scope", "webclient")
                .addFormDataPart("username", email)
                .addFormDataPart("password", password)
                .build();

        Request request = new Request.Builder()
                .url(authenticateClient)
                .method("POST", body)
                .addHeader("Authorization", "Basic " + authStringEnc)
                .build();

        Map<String, String> token = null;
        Type empMapType = new TypeToken<Map<String, String>>() {
            private static final long serialVersionUID = 1L;
        }
                .getType();
        try {
            Response response = client.newCall(request).execute();
            BufferedReader in = new BufferedReader(new InputStreamReader(response.body().byteStream()));

            String responseBody = new BufferedReader(in)
                    .lines()
                    .collect(Collectors.joining("\n"));
            log.debug("responseBody -->{}", responseBody);

            token = gson.fromJson(responseBody, empMapType);

        } catch (Exception e) {
            log.error("Error calling auth service " + e.getMessage());
            throw new ServiceException("Failed to call oauth service");
        }

        log.info("response " + token);

        if (token.get("access_token") == null)
            throw new OAuth2Exception(token.toString());

        return token.get("access_token");
    }
}
