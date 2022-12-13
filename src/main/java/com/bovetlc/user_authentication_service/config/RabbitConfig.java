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
    public static final String CANC_FROM_CLIENT_QUEUE = "canc_from_client_queue";
    public static final String CANC_COMP_FROM_OPS_QUEUE = "canc_complete_from_ops_queue";
    public static final String CLIENT_EXCHANGE = "completion_exchange";
    public static final String ORDER_EXCHANGE = "order_exchange";
    public static final String CANC_FROM_CLIENT_EXCHANGE = "canc_from_client_exchange";
    public static final String CANC_COMP_FROM_OPS_EXCHANGE = "canc_complete_from_ops_exchange";
    public static final String ROUTING_KEY = "message_routingKey";

    @Bean
    public Queue client_queue() { return new Queue(CLIENT_QUEUE); }

    @Bean
    public Queue order_queue() { return new Queue(ORDER_QUEUE); }

    @Bean
    public Queue canc_from_client_queue() { return new Queue(CANC_FROM_CLIENT_QUEUE); }

    @Bean
    public Queue canc_comp_from_ops_queue() { return new Queue(CANC_COMP_FROM_OPS_QUEUE); }

    @Bean
    public DirectExchange client_exchange() {
        return new DirectExchange(CLIENT_EXCHANGE);
    }

    @Bean
    public DirectExchange order_exchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public DirectExchange canc_from_client_exchange() {
        return new DirectExchange(CANC_FROM_CLIENT_EXCHANGE);
    }

    @Bean
    public DirectExchange canc_comp_from_ops_exchange() {
        return new DirectExchange(CANC_COMP_FROM_OPS_EXCHANGE);
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
    @Bean
    public Binding send_canc_binding() {
        return BindingBuilder
                .bind(canc_from_client_queue())
                .to(canc_from_client_exchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding rec_canc_binding() {
        return BindingBuilder
                .bind(canc_comp_from_ops_queue())
                .to(canc_comp_from_ops_exchange())
                .with(ROUTING_KEY);
    }
}
