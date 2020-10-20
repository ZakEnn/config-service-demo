package com.config.persistence;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import lombok.extern.apachecommons.CommonsLog;

@Service
@Qualifier("mongoPropertiesLoader")
@CommonsLog
public class MongoPropertiesLoader implements PropertiesLoader {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insertQuery(String serviceName, String profile, String key, String value) {
		Query searchQuery = new Query(Criteria.where("profile").is(profile));
		Document docService = mongoTemplate.findOne(searchQuery, Document.class, serviceName);
		Map<String, Object> sourceData = getSourceMap(key, value);

		if (docService != null) {
			Map<String, Object> sourceVal = (Map<String, Object>) docService.get("source");

			sourceVal.putAll(sourceData);

			docService.replace("source", sourceVal);

			log.info("service : " + serviceName + "/ configData : " + docService);
			mongoTemplate.save(docService, serviceName);

		} else {
			Map<String, Object> configData = new HashMap<>();

			configData.put("label", "latest");
			configData.put("profile", profile);
			configData.put("source", sourceData);

			log.info("service : " + serviceName + "/ configData : " + configData);

			DBObject docObject = new BasicDBObject(configData);
			mongoTemplate.save(docObject, serviceName);

		}

	}

	private Map<String, Object> getSourceMap(String key, Object value) {
		int end = key.length();
		for (int start; (start = key.lastIndexOf('.', end - 1)) != -1; end = start)
			value = new HashMap<>(Collections.singletonMap(key.substring(start + 1, end), value));
		return new HashMap<>(Collections.singletonMap(key.substring(0, end), value));
	}

}
