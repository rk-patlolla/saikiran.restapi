package com.restapi.app.restapiapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.restapi.app")
@EnableJpaRepositories(basePackages = "com.restapi.app")
@EntityScan(basePackages = "com.restapi.app")
public class RestapiappApplication  {

	public static void main(String[] args) {
		SpringApplication.run(RestapiappApplication.class, args);
	}
	
	
	
	
	
}
