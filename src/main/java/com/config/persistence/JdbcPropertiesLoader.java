package com.config.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Qualifier("jdbcPropertiesLoader")
public class JdbcPropertiesLoader implements PropertiesLoader {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void insertQuery(String serviceName, String profile, String key, String value) {
		jdbcTemplate.update("insert into properties (application, profile, key, value) values(?,?,?,?)", serviceName,
				profile, key, value);
	}

}
