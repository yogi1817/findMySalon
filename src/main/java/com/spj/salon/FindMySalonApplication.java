package com.spj.salon;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.spj.salon.config.ServiceConfig;
import com.spj.salon.utils.UserContextInterceptor;


@SpringBootApplication
@ComponentScan(basePackages = "com.spj.salon")
@EnableJpaRepositories(basePackages =  {"com.spj.salon.barber.repository", "com.spj.salon.user.repository",
		"com.spj.salon.services.repository", "com.spj.salon.checkin.repository"})
@EntityScan(basePackages = {"com.spj.salon.barber.model", "com.spj.salon.user.model", 
		"com.spj.salon.services.model", "com.spj.salon.checkin.model"})
public class FindMySalonApplication {

	@Autowired
	ServiceConfig serviceConfig;
	
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
		if(interceptors == null) {
			template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
		}else {
			interceptors.add(new UserContextInterceptor());
			template.setInterceptors(interceptors);
		}
		return template;
	}
	
	//Uncomment below code if you want to use goofle api for location
	/*@Bean
	public GeoApiContext getGeoApiContext() {
		return new GeoApiContext.Builder()
			    .apiKey(serviceConfig.getGoogleApiKey())
			    .build();
	}
	
	@PreDestroy
    public void preDestroy() {
		getGeoApiContext().shutdown();
    }*/
	
}