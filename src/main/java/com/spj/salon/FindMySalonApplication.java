package com.spj.salon;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import com.spj.salon.config.EnvironmentConfig;
import com.spj.salon.config.ServiceConfig;
import com.spj.salon.utils.UserContextInterceptor;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
@RequiredArgsConstructor
public class FindMySalonApplication {

    private final ServiceConfig serviceConfig;
    private final EnvironmentConfig envConfig;

    public static void main(String[] args) {
        SpringApplication.run(FindMySalonApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Primary
    @Bean
    public RestTemplate getCustomRestTemplete() {
        RestTemplate template = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
        if (interceptors == null || interceptors.isEmpty()) {
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        } else {
            interceptors.add(new UserContextInterceptor());
            template.setInterceptors(interceptors);
        }
        return template;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(serviceConfig.getMailHost());
        mailSender.setPort(serviceConfig.getMailPort());

        mailSender.setUsername(envConfig.getMailUsername());
        mailSender.setPassword(envConfig.getMailPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

    // Uncomment below code if you want to use goofle api for location
    /*
     * @Bean public GeoApiContext getGeoApiContext() { return new
     * GeoApiContext.Builder() .apiKey(serviceConfig.getGoogleApiKey()) .build(); }
     *
     * @PreDestroy public void preDestroy() { getGeoApiContext().shutdown(); }
     */

}