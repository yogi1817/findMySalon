package com.spj.salon;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.spj.salon.configs.EnvironmentConfig;
import com.spj.salon.configs.ServiceConfig;
import com.spj.salon.otp.gmail.GmailCredentials;
import com.spj.salon.otp.gmail.GmailServiceImpl;
import com.spj.salon.otp.ports.out.GmailService;
import lombok.RequiredArgsConstructor;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.TimeZone;

@SpringBootApplication
@EnableResourceServer
@RequiredArgsConstructor
public class FindMySalonApplication {

    private final ServiceConfig serviceConfig;
    private final EnvironmentConfig envConfig;

    @PostConstruct
    public void init() {
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
        SpringApplication.run(FindMySalonApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        ClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
        return new RestTemplate(requestFactory);
    }

    /*@Primary
    @Bean
    public RestTemplate getCustomRestTemplate() {
        RestTemplate template = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
        if (interceptors.isEmpty()) {
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        } else {
            interceptors.add(new UserContextInterceptor());
            template.setInterceptors(interceptors);
        }
        return template;
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GmailService getJavaMailSender() {
        GmailService gmailService = null;
        try {
            gmailService = new GmailServiceImpl(GoogleNetHttpTransport.newTrustedTransport());
            gmailService.setGmailCredentials(GmailCredentials.builder()
                    .userEmail(envConfig.getEmail())
                    .clientId(envConfig.getClientId())
                    .clientSecret(envConfig.getClientSecret())
                    .accessToken(envConfig.getAccessToken())
                    .refreshToken(envConfig.getRefreshToken())
                    .build());
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return gmailService;
    }

    // Uncomment below code if you want to use google api for location
    /*
     * @Bean public GeoApiContext getGeoApiContext() { return new
     * GeoApiContext.Builder() .apiKey(serviceConfig.getGoogleApiKey()) .build(); }
     *
     * @PreDestroy public void preDestroy() { getGeoApiContext().shutdown(); }
     */
}