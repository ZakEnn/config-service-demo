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
		String localConfigPath = getLocalPath(app, profile, type);

		log.info("local config path = " + localConfigPath);
		try {
			PropertiesConfiguration configuration = new PropertiesConfiguration(localConfigPath);
			Properties properties = ConfigurationConverter.getProperties(configuration);
			Enumeration<?> enums = properties.propertyNames();

			while (enums.hasMoreElements()) {
				String key = String.valueOf(enums.nextElement());
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
		if ("yml".equalsIgnoreCase(type) || "yaml".equalsIgnoreCase(type)) {
			return localPath.concat(".yml");
		}
		return localPath.concat(".properties");
	}

	private void insertRedisQuery(String app, String profile, String key, String value) {
		String serviceNameWithProfile = app.concat("-").concat(profile);
		redisTemplate.opsForHash().put(serviceNameWithProfile, key, value);
	}

}
