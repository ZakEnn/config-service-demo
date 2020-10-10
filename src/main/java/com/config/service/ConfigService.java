package com.config.service;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.config.exception.NotFoundException;
import com.config.rest.dto.ConfigDto;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class ConfigService {
	@Value("${spring.cloud.config.server.native.searchLocations}")
	private String configPath;

	public void convertPropsToQuery(String app, String profile, String label) {
		try {
			PropertiesConfiguration configuration = new PropertiesConfiguration(
					configPath + app + "-" + profile + ".properties");

			Properties properties = ConfigurationConverter.getProperties(configuration);
			Enumeration<String> enums = (Enumeration<String>) properties.propertyNames();

			while (enums.hasMoreElements()) {

				String key = enums.nextElement();
				String value = properties.getProperty(key);
				System.out.println(key + " : " + value);

				insertQuery(app, profile, label, key, value);

			}

		} catch (ConfigurationException e1) {
			throw new NotFoundException("PROPERTIE_NOT_FOUND");
		}

	}

	private void insertQuery(String app, String profile, String label, String key, String value) {
		ConfigDto configDto = new ConfigDto();
		configDto.setApplication(app);
		configDto.setKey(key);
		configDto.setLabel(label);
		configDto.setProfile(profile);
		configDto.setValue(value);
		// if (!propertiesRepository.findByApplicationAndProfileAndKey(app, profile,
		// key).isPresent()) {
		// log.info("KEY NOT EXIST " + key);
		// propertiesRepository.save(properties);
		// }

	}

}
