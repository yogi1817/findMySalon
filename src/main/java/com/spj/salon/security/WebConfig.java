package com.spj.salon.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class will define cors filter for entire project except oauth.
 * Removed @EnableWebMVC as Swagger Ui was not working
 * @author Yogesh Sharma
 *
 */
@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/findmysalon/**")
		.allowedOrigins("*")
		.allowedMethods("PUT", "DELETE", "POST", "GET", "OPTIONS")
		//.allowedHeaders("header1", "header2", "header3")
		//.exposedHeaders("header1", "header2")
		.allowCredentials(false).maxAge(3600);
    }
}