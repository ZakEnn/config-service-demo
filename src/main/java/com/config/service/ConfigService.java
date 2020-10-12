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

import com.config.exception.NotFoundException;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class ConfigService {
	@Value("${config.native.location:config/}")
	private String configPath;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public void convertPropsToQuery(String app, String profile, String type) {
		String localPath = getLocalPath(app, profile, type);

		log.info("local path = " + localPath);
		try {
			PropertiesConfiguration configuration = new PropertiesConfiguration(localPath);
			Properties properties = ConfigurationConverter.getProperties(configuration);
			Enumeration<String> enums = (Enumeration<String>) properties.propertyNames();

			while (enums.hasMoreElements()) {
				String key = enums.nextElement();
				String value = properties.getProperty(key);
				log.info(key + " : " + value);

				insertRedisQuery(app, profile, key, value);
			}

		} catch (ConfigurationException e1) {
			throw new NotFoundException("PROPERTIE_NOT_FOUND");
		}

	}

	private String getLocalPath(String app, String profile, String type) {
		String localPath = configPath + app + "-" + profile;
		if (type.equalsIgnoreCase("yml") || type.equalsIgnoreCase("yaml")) {
			localPath = localPath.concat(".yml");
		} else if (type.equalsIgnoreCase("properties")) {
			localPath = localPath.concat(".properties");
		}
		return localPath;
	}

	private void insertRedisQuery(String app, String profile, String key, String value) {
		String serviceNameWithProfile = app.concat("-").concat(profile);
		redisTemplate.opsForHash().put(serviceNameWithProfile, key, value);
	}

}
