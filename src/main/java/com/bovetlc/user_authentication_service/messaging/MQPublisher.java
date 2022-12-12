package com.bovetlc.user_authentication_service.messaging;

import com.bovetlc.user_authentication_service.entity.dto.OrderDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class MQPublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void publishClientOrder(OrderDTO order){
        rabbitTemplate.convertAndSend(order);
    }
}
