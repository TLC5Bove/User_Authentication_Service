package com.bovetlc.user_authentication_service.services;

import com.bovetlc.user_authentication_service.entity.OrderRequest;
import com.bovetlc.user_authentication_service.entity.Portfolio;
import com.bovetlc.user_authentication_service.entity.User;
import com.bovetlc.user_authentication_service.entity.dto.OrderDTO;
import com.bovetlc.user_authentication_service.entity.enums.Side;
import com.bovetlc.user_authentication_service.entity.enums.Status;
import com.bovetlc.user_authentication_service.messaging.MQPublisher;
import com.bovetlc.user_authentication_service.repository.OrderRequestRepository;
import com.bovetlc.user_authentication_service.repository.PortfolioRepository;
import com.bovetlc.user_authentication_service.repository.UserRepository;
import com.bovetlc.user_authentication_service.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderRequestService {
    private final OrderRequestRepository orderRepository;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final UserStockService userStockService;
    private final JwtUtils jwtUtils;
    private final OrderValidationService orderValidationService;

    @Autowired
    MQPublisher mqPublisher;

    private String getUsername(String authToken){
        return jwtUtils.extractUsername(authToken);
    }

    // create an order
    // get an order
    // get all orders
    // update order status
    // update quantity
    // cancel order
    public OrderRequest createNewOrderRequest(OrderDTO orderDTO, String token){
        String username = getUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalStateException("" +
                        "user with username "+ username+" " +
                        "does not exist.")
        );
        Long portId = orderDTO.getPortId();
        Portfolio portfolio = portfolioRepository.findById(portId).orElseThrow(
                () -> new IllegalStateException("" +
                        "Portfolio with id "+ portId +" " +
                        "does not exist.")
        );
        orderDTO.setOSID(UUID.randomUUID().toString());
        System.out.println("OSID: "+ orderDTO.getOSID());

        OrderRequest order = new OrderRequest(
                orderDTO.getProduct(),
                orderDTO.getQuantity(),
                orderDTO.getPrice(),
                orderDTO.getSide(),
                orderDTO.getType(),
                user,
                orderDTO.getOSID(),
                portfolio
        );

        if ((order.getSide() == Side.BUY) && orderValidationService.balanceIsEnoughToBuy(order)) {
            mqPublisher.publishClientOrder(orderDTO);
            Double currentBalance = user.getBalance();
            user.setBalance(currentBalance - order.getTotalPrice());
            orderRepository.save(order);
        }else {
            return new OrderRequest();
        }

        if ((order.getSide() == Side.SELL) && !orderValidationService.isNotUserTheOwnerOfStock(order)){
            mqPublisher.publishClientOrder(orderDTO);
            orderRepository.save(order);
        }else {
            return new OrderRequest();
        }
        System.out.println(order);
        return order;
    }

    public OrderRequest getAnOrderById(Long id, String token){
        String username = getUsername(token);

        OrderRequest order = orderRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("" +
                        "Order with id "+ id+" " +
                        "does not exist.")
        );

        if (!username.matches(order.getUser().getUsername())){
            throw new IllegalStateException("This user does not own this order");
        }
        return order;
    }

    public OrderRequest getAnOrderByOsId(String osid){
        return orderRepository.findByOSID(osid).orElseThrow(
                () -> new IllegalStateException("" +
                        "Order with OsId "+ osid +" " +
                        "does not exist.")
        );
    }

    public void updateOrderRequest(OrderRequest order){
        orderRepository.save(order);
    }

    public List<OrderRequest> getAllUserOrders(String token){
        String username = getUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalStateException("User with username" + username + " " +
                        "does not exist"));

        return orderRepository.findAllByUser(user).orElseThrow(() ->
                new IllegalStateException("User has no orders"));
    }

    public OrderRequest updateOrderStatus(Status status, Long id, String token){
        OrderRequest orderRequest = getAnOrderById(id, token);

        orderRequest.setStatus(status);
        orderRepository.save(orderRequest);

        return orderRequest;
    }

    public String cancelOrderRequest(Long id, String token){
        OrderRequest orderRequest = getAnOrderById(id, token);

        orderRequest.setStatus(Status.CANCELLED);
        orderRepository.save(orderRequest);

        // TODO: SEND CANCEL REQUEST TO OPS
        return "Order Cancelled";
    }
}
