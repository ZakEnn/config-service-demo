package com.easysign.service;

import org.springframework.stereotype.Service;

import lombok.Data;

@Data
@Service
public class ConfigData {
	private String key;
	private String value;
	private String serviceName;

}
