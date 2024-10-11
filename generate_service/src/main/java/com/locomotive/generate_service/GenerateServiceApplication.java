package com.locomotive.generate_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GenerateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenerateServiceApplication.class, args);
	}

}
