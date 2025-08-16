package com.fitness.activityService.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    //declare a queue nae activity queue in rabbitMQ
    // durable means when rabbitmq is restart this queue will be remain, it will not b removed.
    @Bean
    public Queue activityQueue(){
        return new Queue("activity.queue",true);
    }
    // objects convertes to json
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
