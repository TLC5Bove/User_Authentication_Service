package com.bovetlc.user_authentication_service.messaging;

import com.bovetlc.user_authentication_service.config.RabbitConfig;
import com.bovetlc.user_authentication_service.entity.dto.OrderDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQPublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void publishClientOrder(OrderDTO order){ rabbitTemplate.convertAndSend(RabbitConfig.ORDER_EXCHANGE, RabbitConfig.ROUTING_KEY, order); }

    public void publishCancelOrder(String osid){ rabbitTemplate.convertAndSend(RabbitConfig.CANC_FROM_CLIENT_EXCHANGE, RabbitConfig.ROUTING_KEY, osid); }
}
