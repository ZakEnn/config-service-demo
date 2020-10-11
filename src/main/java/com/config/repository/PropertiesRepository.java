package com.config.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.config.entities.ConfigData;

@Repository
public interface PropertiesRepository extends CrudRepository<ConfigData, String> {
	public Optional<ConfigData> findByApplicationAndProfileAndKey(String app, String profile, String key);
}
