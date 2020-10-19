package com.config.persistence;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.apachecommons.CommonsLog;

@Service
@Qualifier("mongoPropertiesLoader")
@CommonsLog
public class MongoPropertiesLoader implements PropertiesLoader {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insertQuery(String serviceName, String profile, String key, String value) {
		if (!mongoTemplate.collectionExists(serviceName)) {
			mongoTemplate.createCollection(serviceName);
		}
		Map<String, Object> configData = new HashMap<>();
		configData.put("label", "latest");
		configData.put("profile", profile);
		configData.put("source", getSourceMap(key, value));

		log.info("service : " + serviceName + "/ configData : " + configData);
		mongoTemplate.save(configData, serviceName);
	}

	private Map<String, Object> getSourceMap(String key, Object value) {
		int end = key.length();
		for (int start; (start = key.lastIndexOf('.', end - 1)) != -1; end = start)
			value = new HashMap<>(Collections.singletonMap(key.substring(start + 1, end), value));
		return new HashMap<>(Collections.singletonMap(key.substring(0, end), value));

	}

}
