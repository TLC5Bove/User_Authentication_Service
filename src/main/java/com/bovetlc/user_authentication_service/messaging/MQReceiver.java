package com.bovetlc.user_authentication_service.messaging;

import com.bovetlc.user_authentication_service.config.RabbitConfig;
import com.bovetlc.user_authentication_service.services.OrderRequestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQReceiver {
    @Autowired
    OrderRequestService orderRequestService;

    @RabbitListener(queues = RabbitConfig.CLIENT_QUEUE)
    public void listener(String osid, Integer quantity){
        // Get order by osid from db
        // Update the that order's quantity and mark status as completed
        //send another message to the reporting service
        System.out.println("Order has been received");
    }
}
