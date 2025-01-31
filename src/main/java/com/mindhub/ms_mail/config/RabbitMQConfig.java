package com.mindhub.ms_mail.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "app.exchange";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";
    public static final String ORDER_CREATED_QUEUE = "order.created.queue";
    public static final String USER_CREATED_ROUTING_KEY = "user.created";
    public static final String USER_CREATED_QUEUE = "user.created.queue";
    public static final String USER_VALIDATED_QUEUE = "user.validated.queue";
    public static final String USER_VALIDATED_ROUTING_KEY = "user.validated";


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue userCreatedQueue() {
        return new Queue(USER_CREATED_QUEUE, true);
    }

    @Bean
    public Queue userValidatedQueue() {
        return new Queue(USER_VALIDATED_QUEUE, true);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, true);
    }

    @Bean
    public Binding userCreatedBinding(Queue userCreatedQueue, TopicExchange appExchange) {
        return BindingBuilder.bind(userCreatedQueue).to(appExchange).with(USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding userValidatedBinding(Queue userValidatedQueue, TopicExchange appExchange) {
        return BindingBuilder.bind(userValidatedQueue).to(appExchange).with(USER_VALIDATED_ROUTING_KEY);
    }

    @Bean
    public Binding orderCreatedBinding(Queue orderCreatedQueue, TopicExchange appExchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(appExchange).with(ORDER_CREATED_ROUTING_KEY);
    }
}

