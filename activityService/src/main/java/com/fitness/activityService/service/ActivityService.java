package com.fitness.activityService.service;

import com.fitness.activityService.dto.ActivityRequest;
import com.fitness.activityService.dto.ActivityResponse;
import com.fitness.activityService.model.Activity;
import com.fitness.activityService.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor // this will only make constriuctor for feilds whihc are marked final
// but allArgconstuctor will make constructor for all the feilds that are present in that class
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    //hepls to send and recive messages to rabiitmq
    private final RabbitTemplate rabbitTemplate;

    //we are getting these properties from application.properties files
    @Value("${rabbitmq.exchange.name}")
    private String exchange;


    @Value("${rabbitmq.routing.key}")
    private String routingKey;


    public ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValidUser = userValidationService.validateUser(request.getUserId());

        if(!isValidUser){
            throw new RuntimeException("Invalid User" + request.getUserId());
        }

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        //Publish to rabbitMQ for AI processing
        try{
            rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);
        }catch (Exception e){
            log.error("Failed to publish activity to RabbitMQ : ",e);
        }

        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());

        return response;
    }



    public List<ActivityResponse> getUserActivities(String userId) {

        List<Activity> activities = activityRepository.findByUserId(userId);
        // here creating a tream of list of activities
        // then mapping each activity to a ActovityRepsosnse by calling a function mapToResponse
        // And then collecting them as ToList

        return activities.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ActivityResponse getActivityByID(String activityId) {

        return activityRepository.findById(activityId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
    }
}
