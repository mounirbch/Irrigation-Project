package com.mongo.projetPFE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication

public class ProjetPfeApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProjetPfeApplication.class, args);
	}


	/*@Bean
	CommandLineRunner runner (ProductRepository repository){
		return args ->{
			Product product=new Product(
					"mounir",
			        "mounir"
			);
			repository.insert(product);
		};

	}*/


	}







