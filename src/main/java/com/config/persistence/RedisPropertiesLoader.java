package com.config.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Qualifier("redisPropertiesLoader")
public class RedisPropertiesLoader implements PropertiesLoader {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void insertQuery(String serviceName, String profile, String key, String value) {
		String serviceNameWithProfile = serviceName.concat("-").concat(profile);
		redisTemplate.opsForHash().put(serviceNameWithProfile, key, value);
	}

}
