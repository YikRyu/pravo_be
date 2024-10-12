package com.example.pravo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@PropertySource("classpath:application.yaml")
public class pravoApplication {

	public static void main(String[] args) {
		SpringApplication.run(pravoApplication.class, args);
	}

}
