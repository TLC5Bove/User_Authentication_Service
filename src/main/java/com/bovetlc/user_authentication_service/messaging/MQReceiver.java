package com.bovetlc.user_authentication_service.messaging;

import com.bovetlc.user_authentication_service.config.RabbitConfig;
import com.bovetlc.user_authentication_service.entity.OrderRequest;
import com.bovetlc.user_authentication_service.entity.enums.Status;
import com.bovetlc.user_authentication_service.services.OrderRequestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQReceiver {
    @Autowired
    OrderRequestService orderRequestService;

    @RabbitListener(queues = RabbitConfig.CLIENT_QUEUE)
    public void listener(String osid){
        OrderRequest order = orderRequestService.getAnOrderByOsId(osid);
        order.setStatus(Status.COMPLETE);
        orderRequestService.updateOrderRequest(order);
    }
}
