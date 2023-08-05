package com.prueba.aplicacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class TecnicaAbstractApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TecnicaAbstractApplication.class);
		String port = System.getenv("PORT");
		app.setDefaultProperties(Collections.singletonMap("server.port", port == null ? "8080" : port));
		app.run(args);
	}

}
