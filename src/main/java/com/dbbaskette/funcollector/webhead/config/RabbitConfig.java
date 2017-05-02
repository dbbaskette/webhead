package com.dbbaskette.funcollector.webhead.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Queue;


/**
 * Created by dbaskette on 4/4/17.
 */

@SpringBootConfiguration
public class RabbitConfig {

    private final ConnectionFactory connectionFactory;

    @Autowired
    public RabbitConfig(ConnectionFactory connectionFactory){

        this.connectionFactory = connectionFactory;
    }

    @Bean
    public RabbitTemplate template() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(this.connectionFactory);
        rabbitTemplate.setRoutingKey("vision-ready");
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue visionreadyQ(){
        return new Queue("vision-ready", false);
    }
}
