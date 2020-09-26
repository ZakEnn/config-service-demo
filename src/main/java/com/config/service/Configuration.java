package com.config.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class Configuration {

	public void initHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

	}

	public void saveConfig(ConfigData config) {

		String cwd = new File("").getAbsolutePath();
		PropertiesConfiguration conf;

		try {

			Properties prop = new Properties();

			conf = new PropertiesConfiguration(
					cwd + "/config/" + config.getServiceName() + "-" + config.getProfile() + ".properties");
			File file = conf.getFile();
			InputStream in;
			try {
				in = new FileInputStream(file);
				prop.load(in);

			} catch (IOException e) {
				log.warn(e.getMessage());
			}

			conf.addProperty(config.getKey(), config.getValue());

			conf.save();

		} catch (ConfigurationException e) {
			log.warn(e.getMessage());
		}

	}

	public void updateConfig(ConfigData config) {

		String cwd = new File("").getAbsolutePath();
		PropertiesConfiguration conf;

		try {
			conf = new PropertiesConfiguration(
					cwd + "/config/" + config.getServiceName() + "-" + config.getProfile() + ".properties");

			conf.setProperty(config.getKey(), config.getValue());

			conf.save();

		} catch (ConfigurationException e) {
			log.warn(e.getMessage());
		}

	}

	public void removeConfig(ConfigData config) {
		String cwd = new File("").getAbsolutePath();
		PropertiesConfiguration conf;

		try {
			conf = new PropertiesConfiguration(
					cwd + "/config/" + config.getServiceName() + "-" + config.getProfile() + ".properties");
			conf.clearProperty(config.getKey());
			conf.save();

		} catch (ConfigurationException e) {
			log.warn(e.getMessage());
		}
	}

	public List<ConfigData> getConfiguration(String serviceName, String profile) throws ConfigurationException {
		String cwd = new File("").getAbsolutePath();
		PropertiesConfiguration conf;

		List<ConfigData> configObj = new ArrayList<>();
		String path = cwd + "/config/" + serviceName + "-" + profile + ".properties";

		Properties prop = new Properties();
		conf = new PropertiesConfiguration(path);
		File file = conf.getFile();
		InputStream in;
		try {
			in = new FileInputStream(file);
			prop.load(in);

		} catch (IOException e) {
			log.warn(e.getMessage());
		}

		ConfigData data = new ConfigData();
		for (Object key : prop.keySet()) {
			data.setKey(key.toString());
			data.setValue(prop.getProperty((String) key));
			data.setServiceName(serviceName);
			data.setProfile(profile);
			data.setLabel("default");
			configObj.add(data);
		}

		return configObj;

	}

}
