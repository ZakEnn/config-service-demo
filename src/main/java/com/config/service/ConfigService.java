package com.config.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
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
	private PropertiesLoader mongoPropertiesLoader;

	public void loadPropsToDB(String app, String profile, String type) {
		String localConfigPath = getLocalPath(app, profile, type);
		Properties properties;
		if (isYamlFile(localConfigPath)) {
			properties = getPropertiesFromYaml(localConfigPath);
		} else {
			properties = getProperties(localConfigPath);
		}

		loadLocalConfToDB(app, profile, properties);
	}

	private boolean isYamlFile(String localConfigPath) {
		return localConfigPath.toUpperCase().endsWith(".YAML") || localConfigPath.toUpperCase().endsWith(".YML");
	}

	private void loadLocalConfToDB(String app, String profile, Properties properties) {
		properties.keySet().forEach(propKey -> {
			String key = String.valueOf(propKey);
			String value = properties.getProperty(key);
			log.info(key + " : " + value);

			mongoPropertiesLoader.insertQuery(app, profile, key, value);
		});
	}

	private Properties getPropertiesFromYaml(String localConfigPath) {
		YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
		try {
			Resource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(localConfigPath)));
			factory.setResources(resource);
		} catch (IOException e) {
			log.warn(e.getMessage());
		}

		factory.afterPropertiesSet();
		return factory.getObject();
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
