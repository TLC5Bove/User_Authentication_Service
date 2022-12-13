package com.bovetlc.user_authentication_service.messaging;

import com.bovetlc.user_authentication_service.config.RabbitConfig;
import com.bovetlc.user_authentication_service.entity.OrderRequest;
import com.bovetlc.user_authentication_service.entity.dto.CompleteOrder;
import com.bovetlc.user_authentication_service.entity.enums.Side;
import com.bovetlc.user_authentication_service.entity.enums.Status;
import com.bovetlc.user_authentication_service.messaging.dto.OsidQuantityPrice;
import com.bovetlc.user_authentication_service.services.OrderRequestService;
import com.bovetlc.user_authentication_service.services.PortfolioService;
import com.bovetlc.user_authentication_service.services.UserStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MQReceiver {
    @Autowired
    private final OrderRequestService orderRequestService;

    private final UserStockService userStockService;

    private final PortfolioService portfolioService;

    @RabbitListener(queues = RabbitConfig.CLIENT_QUEUE)
    public void listener(CompleteOrder completeOrder){
        String osid = completeOrder.getOSID();
        Double cummPrice = completeOrder.getCummPrice();

        OrderRequest order = orderRequestService.getAnOrderByOsId(osid);
        order.setStatus(Status.COMPLETE);
        order.setCummPrice(cummPrice);
        orderRequestService.updateOrderRequest(order);

        Long portId = order.getPortfolio().getId();

        if (order.getSide() == Side.BUY){
            userStockService.updateStockQuantityInAPortfolio(
                    portId,
                    order.getQuantity(),
                    order.getTicker());
            if (cummPrice < order.getTotalPrice()){
                Double diff = order.getTotalPrice() - cummPrice;
                Double bal = order.getUser().getBalance();
                order.getUser().setBalance(bal + diff);
            }
            portfolioService.updatePortfolioValue(portId, order.getCummPrice());
        }else{
            int quantity = order.getQuantity()*-1;
            userStockService.updateStockQuantityInAPortfolio(
                    order.getPortfolio().getId(),
                    quantity,
                    order.getTicker());

            Double bal = order.getUser().getBalance();
            order.getUser().setBalance(bal - cummPrice);
        }
    }

    @RabbitListener(queues = RabbitConfig.CANC_COMP_FROM_OPS_EXCHANGE)
    public void cancelListener(OsidQuantityPrice obj){
        OrderRequest order = orderRequestService.getAnOrderByOsId(obj.getOsId());
        order.setStatus(Status.CANCELLED);
        order.setQuantity(obj.getQuantity());
        order.setPrice(obj.getPrice());
        orderRequestService.updateOrderRequest(order);
    }
}
