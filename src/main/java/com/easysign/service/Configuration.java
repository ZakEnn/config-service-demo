package com.easysign.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Configuration {
	private HttpHeaders headers;
	private RestTemplate restTemplate = new RestTemplate();
	private Map config = new HashMap<String, Map>();
	private Map discoveryConfig = new HashMap<>();

	public void initHeader() {
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

	}

	public void saveConfig(ConfigData config) {

		String cwd = new File("").getAbsolutePath();
		PropertiesConfiguration conf;

		try {

			String path = cwd + "/config/" + config.getServiceName() + ".properties";

			Properties prop = new Properties();

			conf = new PropertiesConfiguration(cwd + "/config/" + config.getServiceName() + ".properties");
			File file = conf.getFile();
			InputStream in;
			try {
				in = new FileInputStream(file);
				prop.load(in);

			} catch (IOException e) {
				e.printStackTrace();
			}

			// for (Object key : prop.keySet()) {
			// System.out.println(key.toString() + ":" + prop.getProperty((String) key));
			// }
			//
			// System.out.println("Configuration path :" + path + "\n conf file name : " +
			// file.getName());

			conf.addProperty(config.getKey(), config.getValue());

			conf.save();

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateConfig(ConfigData config) {

		String cwd = new File("").getAbsolutePath();
		// System.out.println("path test :" + cwd);
		PropertiesConfiguration conf;

		try {
			conf = new PropertiesConfiguration(cwd + "/config/" + config.getServiceName() + ".properties");

			conf.setProperty(config.getKey(), config.getValue());

			// System.out.println("conf updated : " + conf.toString());

			conf.save();

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void removeConfig(ConfigData config) {
		String cwd = new File("").getAbsolutePath();
		// System.out.println("path test :" + cwd);
		PropertiesConfiguration conf;

		try {
			conf = new PropertiesConfiguration(cwd + "/config/" + config.getServiceName() + ".properties");
			conf.clearProperty(config.getKey());
			// System.out.println("conf removed : " + conf.toString());

			conf.save();

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setConfig(String serviceName) {
		initHeader();

		HttpEntity requestEntity = new HttpEntity<>(headers);

		ResponseEntity<Map> responseEntity = restTemplate.exchange("http://localhost:8888/" + serviceName + "/default",
				HttpMethod.GET, requestEntity, Map.class);
		config.put(serviceName, responseEntity.getBody());
	}

	public Map getConfiguration(String serviceName) throws ConfigurationException {
		String cwd = new File("").getAbsolutePath();
		// System.out.println("path test :" + cwd);
		PropertiesConfiguration conf;

		config = new HashMap<String, Map>();
		Map configObj = new HashMap<String, String>();
		String path = cwd + "/config/" + serviceName + ".properties";

		Properties prop = new Properties();

		// System.out.println("path : " + path);
		conf = new PropertiesConfiguration(path);
		File file = conf.getFile();
		InputStream in;
		try {
			in = new FileInputStream(file);
			prop.load(in);

		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Object key : prop.keySet()) {
			// System.out.println(key.toString() + ":" + prop.getProperty((String) key));
			configObj.put(key.toString(), prop.getProperty((String) key));
		}

		// System.out.println("configObj : " + configObj.toString());
		// System.out.println("\n path :" + path + "\n conf file name : " +
		// file.getName());
		return configObj;

	}

	public Map getConfig(String serviceName) {
		// System.out.println("config: " + config);
		return (Map) config.get(serviceName);
	}

}
