package com.bovetlc.user_authentication_service.controller;

import com.bovetlc.user_authentication_service.entity.OrderRequest;
import com.bovetlc.user_authentication_service.entity.dto.OrderDTO;
import com.bovetlc.user_authentication_service.messaging.MQPublisher;
import com.bovetlc.user_authentication_service.services.OrderRequestService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
class OrderController {

    private final OrderRequestService orderRequestService;

    @Autowired
    MQPublisher publisher;

    @PostMapping("/")
    public ResponseEntity<OrderRequest> placeOrder(@RequestBody OrderDTO orderDTO, @RequestHeader HttpHeaders request){
        String token = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        OrderRequest order = orderRequestService.createNewOrderRequest(orderDTO, token);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderRequest>> getAllUserOrders(@RequestHeader HttpHeaders headers){
        String token = Objects.requireNonNull(headers.getFirst(AUTHORIZATION)).substring(7);
        return ResponseEntity.ok(orderRequestService.getAllUserOrders(token));
    }

    // get an order by id
    // get an order by osid
    // get all orders by a user
    //

    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderRequest> deleteOrder(@PathVariable Long orderId, @RequestHeader HttpHeaders request){
        String token = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        OrderRequest order = orderRequestService.getAnOrderById(orderId, token);
        publisher.publishCancelOrder(order.getOSID());
        return ResponseEntity.ok(order);
    }
}