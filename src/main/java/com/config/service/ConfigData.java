package com.config.service;

import lombok.Data;

@Data
public class ConfigData {
	private String serviceName;
	private String key;
	private String value;
	private String type;
	private String profile;
	private String label;
}
