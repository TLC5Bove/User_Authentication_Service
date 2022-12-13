package com.bovetlc.user_authentication_service.services;

import com.bovetlc.user_authentication_service.entity.OrderRequest;
import com.bovetlc.user_authentication_service.entity.UserStock;
import com.bovetlc.user_authentication_service.repository.UserStockRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderValidationService {

    private final UserStockRepository userStockRepository;

    public Boolean balanceIsEnoughToBuy(OrderRequest order){
        Double balance = order.getUser().getBalance();
        Double totalPrice = order.getTotalPrice();

        return balance >= totalPrice;
    }

    public Boolean isNotUserTheOwnerOfStock(OrderRequest order){
        List<UserStock> stocks = userStockRepository.findAllByPortfolio(order.getPortfolio()).
                orElseThrow(() -> new IllegalStateException
                        ("User does not have such Portfolio"));
        UserStock stock = stocks.stream().filter(stocked -> stocked.getProduct() == order.getTicker()).findFirst().orElse(null);

        return stock == null;
    }

}
