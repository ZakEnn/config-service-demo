package com.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfServiceApplication.class, args);
	}

	// @Bean
	// public RedisTemplate<Long, Book> redisTemplate(RedisConnectionFactory
	// connectionFactory) {
	// RedisTemplate<Long, Book> template = new RedisTemplate<>();
	// template.setConnectionFactory(connectionFactory);
	// // Add some specific configuration here. Key serializers, etc.
	// return template;
	// }

}
