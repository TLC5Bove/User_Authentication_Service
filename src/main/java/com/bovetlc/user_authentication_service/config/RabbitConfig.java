package com.bovetlc.user_authentication_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String CLIENT_QUEUE = "client_queue";
    public static final String ORDER_QUEUE = "order_queue";
    public static final String CLIENT_EXCHANGE = "completion_exchange";
    public static final String ORDER_EXCHANGE = "order_exchange";
    public static final String ROUTING_KEY = "message_routingKey";

    @Bean
    public Queue client_queue() { return new Queue(CLIENT_QUEUE); }

    @Bean
    public Queue order_queue() { return new Queue(ORDER_QUEUE); }

    @Bean
    public DirectExchange client_exchange() {
        return new DirectExchange(CLIENT_EXCHANGE);
    }

    @Bean
    public DirectExchange order_exchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding client_binding() {
        return BindingBuilder
                .bind(client_queue())
                .to(client_exchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding order_binding() {
        return BindingBuilder
                .bind(order_queue())
                .to(order_exchange())
                .with(ROUTING_KEY);
    }
}
