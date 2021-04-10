package com.spj.salon.user.adapters;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.spj.salon.configs.ServiceConfig;
import com.spj.salon.openapi.resources.AuthenticationResponse;
import com.spj.salon.user.ports.out.OAuthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.service.spi.ServiceException;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serial;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Yogesh Sharma
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class OAuthAdapter implements OAuthClient {

    private final Gson gson;
    private final ServiceConfig serviceConfig;

    /**
     * This method calls the oauth service with login id and password and
     * generates the jwt token.
     * This is needed because the UI(angular) was not able to call the OAUTh service provided by spring security
     *
     * @param email
     * @param password
     * @param clientHost
     * @return
     */
    @Override
    public AuthenticationResponse getAuthenticationData(String email, String password, String clientHost) throws OAuth2Exception {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "password")
                .addFormDataPart("scope", "read")
                .addFormDataPart("username", email)
                .addFormDataPart("password", password)
                .build();

        return getAuthData(clientHost, body, email);
    }

    @Override
    public AuthenticationResponse getRefreshToken(String refreshToken, String email, String clientHost) throws OAuth2Exception {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "refresh_token")
                .addFormDataPart("scope", "read")
                .addFormDataPart("refresh_token", refreshToken)
                .build();

        return getAuthData(clientHost, body, email);
    }

    private AuthenticationResponse getAuthData(String clientHost, RequestBody body, String email) {
        String authenticateClient = clientHost + serviceConfig.getAuthenticateService();

        String authString = serviceConfig.getApplicationId() + ":" + serviceConfig.getApplicationPassword();
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        Request request = new Request.Builder()
                .url(authenticateClient)
                .method("POST", body)
                .addHeader("Authorization", "Basic " + authStringEnc)
                .build();

        Map<String, String> token;
        Type empMapType = new TypeToken<Map<String, String>>() {
            @Serial
            private static final long serialVersionUID = 1L;
        }
                .getType();
        try {
            Response response = client.newCall(request).execute();
            BufferedReader in = new BufferedReader(new InputStreamReader(Objects.requireNonNull(response.body()).byteStream()));

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

        if (!email.equals(token.get("email")))
            throw new OAuth2Exception("Invalid email passed");

        return new AuthenticationResponse()
                .accessToken(token.get("access_token"))
                .refreshToken(token.get("refresh_token"))
                .email(email);
    }
}
