package com.config.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.config.service.ConfigService;

@RestController
public class ConfigRestController {
	@Autowired
	ConfigService configService;

	@GetMapping(value = "/properties/load-to-db/{application}/profile/{profile}")
	public ResponseEntity<Object> convertProps(@PathVariable String application, @PathVariable String profile,
			@RequestParam(required = false) String type) {
		configService.loadPropsToDB(application, profile, type);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
