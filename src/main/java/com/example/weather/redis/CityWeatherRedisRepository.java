package com.example.weather.redis;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.weather.City;

@Repository
public class CityWeatherRedisRepository {
	
	private RedisTemplate redisTemplate;
	
	public City save(City city) {
		redisTemplate.opsForHash().put("City",city.getName(),city);
		return city;
	}
	
	public List<City> findAll(){
		return redisTemplate.opsForHash().values("City");
	}
	
	public City findCityByName(String name) {
		return (City) redisTemplate.opsForHash().get("City",name);
	}
	
	public void deleteCityByName(String name) {
		redisTemplate.opsForHash().delete("City", name);
	}
}
