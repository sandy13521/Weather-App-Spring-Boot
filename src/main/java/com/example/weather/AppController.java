package com.example.weather;

import java.util.List;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
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
		System.out.println("Home");
		ModelAndView modelAndView = new ModelAndView();
		if(city != null && !city.isEmpty()) {
			City city2 = new City();
			BsonDocument updatedDocument = makeARestCallToOpenWeather(city);
			city2.setName(city);
			city2.setDes(updatedDocument.getArray("weather").get(0).asDocument().get("description").asString().getValue().toString());
			city2.setTime(new BsonTimestamp(System.currentTimeMillis()));
			city2.setIcon(updatedDocument.getArray("weather").get(0).asDocument().get("description").asString().getValue().toString());
			city2.setTemp(new Double(updatedDocument.getDocument("main").asDocument().get("temp").asDouble().getValue()).toString());
			saveCity(city2);
		}
		List<City> listOfCitiesWithDetailsCities = repository.findAll();
		Long currentTimeMillis = System.currentTimeMillis();
		BsonTimestamp currentTimestamp = new BsonTimestamp(currentTimeMillis);
		BsonTimestamp lastUpdatedTimestamp;
		for(int i=0;i<listOfCitiesWithDetailsCities.size();i++) {
			lastUpdatedTimestamp = listOfCitiesWithDetailsCities.get(i).getTime();
			int timeDiff = currentTimestamp.getTime() - lastUpdatedTimestamp.getTime();
			logger.info("timeDifferece between the current Time and last Updated time = " + timeDiff);
			if(timeDiff >= 10) {
				City city2 = new City();
				String cityName = listOfCitiesWithDetailsCities.get(i).getName();
				BsonDocument updatedDocument = makeARestCallToOpenWeather(cityName);
				city2.setName(cityName);
				city2.setDes(updatedDocument.getArray("weather").get(0).asDocument().get("description").asString().toString());
				city2.setTime(new BsonTimestamp(System.currentTimeMillis()));
				city2.setIcon(updatedDocument.getArray("weather").get(0).asDocument().get("description").asString().toString());
				city2.setTemp(updatedDocument.getDocument("main").asDocument().get("temp").asString().toString());
				listOfCitiesWithDetailsCities.set(i, city2);
			}
		}
		modelAndView.addObject("city", listOfCitiesWithDetailsCities);
		modelAndView.setViewName("home.jsp");
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
