package com.config.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.config.service.ConfigService;

@RestController
public class ConfigRestController {
	@Autowired
	ConfigService configService;

	@GetMapping(value = "/properties/convertProps/{app}/{profile}/{label}")
	public ResponseEntity<Object> convertProps(@PathVariable String app, @PathVariable String profile,
			@PathVariable String label) {
		configService.convertPropsToQuery(app, profile, label);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
