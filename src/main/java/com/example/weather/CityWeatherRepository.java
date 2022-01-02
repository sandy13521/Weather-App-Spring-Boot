package com.example.weather;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityWeatherRepository extends MongoRepository<City, String>{

}
