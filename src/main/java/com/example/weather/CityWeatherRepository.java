package com.example.weather;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CityWeatherRepository extends MongoRepository<City, String>{
	
	public List<City> findByName(String name);
	
}
