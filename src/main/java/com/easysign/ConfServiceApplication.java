package com.easysign;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.easysign.service.Configuration;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfServiceApplication.class, args);
	}

	@org.springframework.context.annotation.Configuration
	@EnableWebMvc
	public class WebConfig extends WebMvcConfigurerAdapter {

		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("**").allowedOrigins("http://localhost:4200").allowedMethods("PUT", "DELETE", "POST")
					.allowCredentials(false).maxAge(3600);
		}
	}

	// @Bean
	CommandLineRunner start(Configuration configuration, ArrayList<String> services) {
		return args -> {

			services.add("uaa-service");
			services.add("discovery-service");

			for (String service : services) {
				configuration.setConfig(service);
			}
		};
	}
}
