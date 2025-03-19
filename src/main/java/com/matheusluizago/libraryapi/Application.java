package com.matheusluizago.libraryapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //Pra habilitar a auditoria das anotações de Author
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


}
