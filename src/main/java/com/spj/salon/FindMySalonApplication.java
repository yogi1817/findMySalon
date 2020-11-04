package com.spj.salon;

import com.spj.salon.configs.EnvironmentConfig;
import com.spj.salon.configs.ServiceConfig;
import lombok.RequiredArgsConstructor;
import org.apache.http.impl.client.HttpClients;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@SpringBootApplication
@EnableResourceServer
@RequiredArgsConstructor
public class FindMySalonApplication {

    private final ServiceConfig serviceConfig;
    private final EnvironmentConfig envConfig;

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

    //This is required for JSONNullable in openAPI for BarberCalendarRequest Enum type
    @Bean
    public JsonNullableModule jsonNullableModule() {
        return new JsonNullableModule();
    }
    // Uncomment below code if you want to use google api for location
    /*
     * @Bean public GeoApiContext getGeoApiContext() { return new
     * GeoApiContext.Builder() .apiKey(serviceConfig.getGoogleApiKey()) .build(); }
     *
     * @PreDestroy public void preDestroy() { getGeoApiContext().shutdown(); }
     */

    @Bean
    public ConnectionFactory connectionFactory() {
        String uri = System.getenv("CLOUDAMQP_URL");
        if (uri == null)
            uri = "amqp://guest:guest@localhost";

        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(uri);
        factory.setRequestedHeartBeat(30);
        factory.setConnectionTimeout(30);

        return factory;
    }
}