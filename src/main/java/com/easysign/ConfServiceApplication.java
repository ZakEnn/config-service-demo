package com.easysign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfServiceApplication.class, args);
	}

}