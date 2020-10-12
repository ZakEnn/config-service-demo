package com.config.service;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.config.entities.ConfigData;
import com.config.exception.NotFoundException;
import com.config.repository.PropertiesRepository;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class ConfigService {
	@Value("${spring.cloud.config.server.native.searchLocations:config/}")
	private String configPath;

	@Autowired
	private PropertiesRepository propertiesRepository;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public void convertPropsToQuery(String app, String profile, String label) {
		String localPath = configPath + app + "-" + profile + ".properties";
		log.info("local path = " + localPath);
		try {

			PropertiesConfiguration configuration = new PropertiesConfiguration(
					configPath + app + "-" + profile + ".properties");

			Properties properties = ConfigurationConverter.getProperties(configuration);
			Enumeration<String> enums = (Enumeration<String>) properties.propertyNames();

			while (enums.hasMoreElements()) {

				String key = enums.nextElement();
				String value = properties.getProperty(key);
				log.info(key + " : " + value);

				insertQuery(app, profile, label, key, value);

			}

		} catch (ConfigurationException e1) {
			throw new NotFoundException("PROPERTIE_NOT_FOUND");
		}

	}

	private void insertQuery(String app, String profile, String label, String key, String value) {
		ConfigData configData = new ConfigData();
		configData.setApplication(app);
		configData.setKey(key);
		configData.setLabel(label);
		configData.setProfile(profile);
		configData.setValue(value);
		if (!propertiesRepository.findByApplicationAndProfileAndKey(app, profile, key).isPresent()) {
			log.info("KEY NOT EXIST " + key);
			propertiesRepository.save(configData);
		}
		// add props
		redisTemplate.opsForSet().add("test", "test");
	}

}
