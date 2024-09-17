package com.example.transacciones;


import org.apache.logging.log4j.LogManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class TransaccionesApplication {
	private static final Logger logger = LogManager.getLogger(TransaccionesApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TransaccionesApplication.class, args);
	}
}

