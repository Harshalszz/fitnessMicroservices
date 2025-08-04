package com.fitness.activityService.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
// for update and created at date and time
public class MongoConfig {
}
