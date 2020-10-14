package com.config.service;

import java.util.Properties;

import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.config.exception.NotFoundException;
import com.config.persistence.PropertiesLoader;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class ConfigService {
	@Value("${config.native.location:config/}")
	private String configPath;

	@Autowired
	private PropertiesLoader redisPropertiesLoader;

	public void loadPropsToDB(String app, String profile, String type) {
		String localConfigPath = getLocalPath(app, profile, type);
		Properties properties = getProperties(localConfigPath);

		properties.keySet().forEach(propKey -> {
			String key = String.valueOf(propKey);
			String value = properties.getProperty(key);
			log.info(key + " : " + value);

			redisPropertiesLoader.insertQuery(app, profile, key, value);
		});

	}

	private Properties getProperties(String localConfigPath) {
		try {
			PropertiesConfiguration configuration = new PropertiesConfiguration(localConfigPath);
			return ConfigurationConverter.getProperties(configuration);

		} catch (ConfigurationException e) {
			log.warn(e.getMessage());
			throw new NotFoundException("PROPERTIE_NOT_FOUND");
		}
	}

	private String getLocalPath(String app, String profile, String type) {
		String localPath = configPath + app + "-" + profile;

		if ("yml".equalsIgnoreCase(type) || "yaml".equalsIgnoreCase(type)) {
			return localPath.concat(".yml");
		}

		return localPath.concat(".properties");
	}

}
