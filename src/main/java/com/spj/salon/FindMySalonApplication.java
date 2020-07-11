package com.spj.salon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.spj.salon")
@EnableJpaRepositories(basePackages =  {"com.spj.salon.barber.repository", "com.spj.salon.user.repository",
		"com.spj.salon.services.repository", "com.spj.salon.checkin.repository"})
@EntityScan(basePackages = {"com.spj.salon.barber.model", "com.spj.salon.user.model", 
		"com.spj.salon.services.model", "com.spj.salon.checkin.model"})
public class FindMySalonApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindMySalonApplication.class, args);
	}
}