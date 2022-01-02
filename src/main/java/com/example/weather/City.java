package com.example.weather;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

import org.bson.BsonTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "city")
public class City{
	@Id
	private String id;
	private String name;
	private String temp;
	private String des;
	private String icon;
	private BsonTimestamp time;
}
