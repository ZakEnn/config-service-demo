package com.config.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Data;

@Data
@RedisHash(value = "properties")
public class ConfigData {
	@Id
	@Indexed
	private String id;
	private String application;
	private String profile;
	private String label;
	private String key;
	private String value;
}
