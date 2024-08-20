package com.example.springX;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringXApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringXApplication.class, args);
	}
}
