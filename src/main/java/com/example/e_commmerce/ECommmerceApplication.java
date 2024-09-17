package com.example.e_commmerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost:5174")
public class ECommmerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommmerceApplication.class, args);
	}

}
