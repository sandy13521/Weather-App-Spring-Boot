package com.example.weather;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {
	private final Logger logger = LogManager.getLogger();
	
	@Autowired
	private CityWeatherRepository repository;
	
	private RestTemplate restTemplate;
	private String url = "https://api.openweathermap.org/data/2.5/weather?q=";
	private String apiKey = "183536c5fc25a8b47bac35a65fd48b2e";
	
	@SuppressWarnings("unused")
	@RequestMapping("home")
	public ModelAndView home(String city) {
		logger.info("/home?city=<city_name>");
		ModelAndView modelAndView = new ModelAndView();
		List<City> listOfCitiesInDB = repository.findByName(city);
		if(city != null && !city.isEmpty() && listOfCitiesInDB.size() == 0) {
			logger.info("New City Being Added to DB " + city);
			City city1 = new City();
			BsonDocument updatedDocument = makeARestCallToOpenWeather(city);
			city1.setName(city);
			city1.setDes(updatedDocument.getArray("weather").get(0).asDocument().get("description").asString().getValue().toString());
			city1.setDate(new Date(System.currentTimeMillis()));
			city1.setIcon(updatedDocument.getArray("weather").get(0).asDocument().get("icon").asString().getValue().toString());
			city1.setTemp(String.valueOf(updatedDocument.getDocument("main").asDocument().get("temp").asDouble().getValue()));
			saveCity(city1);
		}
		List<City> listOfCitiesWithDetailsCities = repository.findAll();
		Long currentTimeMillis = System.currentTimeMillis();
		Date currentDate = new Timestamp(currentTimeMillis);
		Date lastUpdatedDate;
		for(int i=0;i<listOfCitiesWithDetailsCities.size();i++) {
			lastUpdatedDate = listOfCitiesWithDetailsCities.get(i).getDate();
			long timeDiff = currentDate.getTime() - lastUpdatedDate.getTime();
			if(TimeUnit.MILLISECONDS.toMinutes(timeDiff) > 10) {
				City city2 = new City();
				String cityName = listOfCitiesWithDetailsCities.get(i).getName();
				BsonDocument updatedDocument = makeARestCallToOpenWeather(cityName);
				city2.setName(cityName);
				city2.setDes(updatedDocument.getArray("weather").get(0).asDocument().get("description").asString().getValue().toString());
				city2.setDate(new Date(System.currentTimeMillis()));
				city2.setIcon(updatedDocument.getArray("weather").get(0).asDocument().get("icon").asString().getValue().toString());
				city2.setTemp(String.valueOf(updatedDocument.getDocument("main").asDocument().get("temp").asDouble().getValue()));
				listOfCitiesWithDetailsCities.set(i, city2);
			}
		}
		modelAndView.addObject("city", listOfCitiesWithDetailsCities);
		modelAndView.setViewName("home.jsp");
		logger.info("Model and view is set and being returned");
		return modelAndView;
	}
	
	private void saveCity(@RequestBody City city) {
		repository.save(city);
	}
	
	private BsonDocument makeARestCallToOpenWeather(String cityName) {
		restTemplate = new RestTemplate();
		String jsonCityString = restTemplate.getForObject(url + cityName + "&units=metric&appid=" + apiKey,String.class);
		BsonDocument city = new JsonObject(jsonCityString).toBsonDocument();
		return city;
	}
}
