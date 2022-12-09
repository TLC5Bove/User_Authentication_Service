package com.bovetlc.user_authentication_service.services;

import com.bovetlc.user_authentication_service.entity.OrderRequest;
import com.bovetlc.user_authentication_service.entity.Portfolio;
import com.bovetlc.user_authentication_service.entity.User;
import com.bovetlc.user_authentication_service.entity.UserStock;
import com.bovetlc.user_authentication_service.entity.dto.OrderDTO;
import com.bovetlc.user_authentication_service.entity.enums.Side;
import com.bovetlc.user_authentication_service.entity.enums.Status;
import com.bovetlc.user_authentication_service.repository.OrderRequestRepository;
import com.bovetlc.user_authentication_service.repository.PortfolioRepository;
import com.bovetlc.user_authentication_service.repository.UserRepository;
import com.bovetlc.user_authentication_service.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderRequestService {
    private final OrderRequestRepository orderRepository;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final UserStockService userStockService;
    private final JwtUtils jwtUtils;

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

        orderRepository.save(order);
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

    public OrderRequest updateOrderQuantity(int quantity, Long id, String token, Long port){
        // add if side is BUY
        // deduct if side is SELL
        OrderRequest orderRequest = getAnOrderById(id, token);
        Portfolio portfolio = orderRequest.getPortfolio();

        int current_quantity = orderRequest.getQuantity();
        if (orderRequest.getSide() == Side.BUY){
            orderRequest.setQuantity(current_quantity+quantity);
        }else{
            orderRequest.setQuantity(current_quantity-quantity);
            quantity *= -1;
        }
        userStockService.updateStockQuantityInAPortfolio(portfolio.getId(), token, quantity, orderRequest.getTicker());
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
