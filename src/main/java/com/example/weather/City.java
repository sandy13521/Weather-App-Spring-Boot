package com.example.weather;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.bson.BsonDateTime;
import org.bson.BsonTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash("City") 
@Document(collection = "city")
public class City implements Serializable{
	@Id
	private String id;
	private String name;
	private String temp;
	private String des;
	private String icon;
	private Date date;
}
