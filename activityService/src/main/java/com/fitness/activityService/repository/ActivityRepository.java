package com.fitness.activityService.repository;

import com.fitness.activityService.model.Activity;
import jdk.jfr.Registered;
import org.springframework.data.mongodb.repository.MongoRepository;

@Registered
public interface ActivityRepository extends MongoRepository<Activity, String> {
}
