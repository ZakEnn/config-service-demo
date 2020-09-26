package com.config.web;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.config.service.ConfigData;
import com.config.service.Configuration;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class ConfigController {

	@Autowired
	private Configuration configuration;

	private RestTemplate restTemplate = new RestTemplate();

	public void refreshConfig() {

		restTemplate.postForObject("http://localhost:8888/actuator/bus-refresh/", null, String.class);

	}

	@GetMapping("/get-config/{serviceName}/profile/{profile}")
	public @ResponseBody List<ConfigData> listConfig(@PathVariable String serviceName, @PathVariable String profile)
			throws ConfigurationException {
		log.info("List configuration from service [{}] : {} ", serviceName, profile);
		return configuration.getConfiguration(serviceName, profile);
	}

	@PostMapping("/add-config")
	public @ResponseBody void addConfig(@RequestBody ConfigData conf) {
		log.info("Add configuration to service [{}] : {}", conf.getServiceName(), conf);
		configuration.saveConfig(conf);
	}

	@PostMapping("/update-config")
	public @ResponseBody void updatingConfig(@RequestBody ConfigData conf) {
		log.info("Update configuration from service [{}]  with : {}", conf.getServiceName(), conf);
		configuration.updateConfig(conf);
	}

	@PostMapping("/remove-config")
	public @ResponseBody void removeConfig(@RequestBody ConfigData conf) {
		log.info("remove configuration line {} from service [{}] with profile {}", conf.getKey(), conf.getServiceName(),
				conf.getProfile());
		configuration.removeConfig(conf);
	}

}
