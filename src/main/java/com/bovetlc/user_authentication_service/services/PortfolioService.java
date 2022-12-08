package com.bovetlc.user_authentication_service.services;

import com.bovetlc.user_authentication_service.entity.Portfolio;
import com.bovetlc.user_authentication_service.entity.User;
import com.bovetlc.user_authentication_service.entity.UserStock;
import com.bovetlc.user_authentication_service.entity.enums.Ticker;
import com.bovetlc.user_authentication_service.repository.PortfolioRespository;
import com.bovetlc.user_authentication_service.repository.UserRepository;
import com.bovetlc.user_authentication_service.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PortfolioRespository portfolioRespository;

    public ResponseEntity<Portfolio> createNewPortfolio(String name, String token){
        String userEmail = jwtUtils.extractUsername(token);
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalStateException("User with email " + userEmail +" does not exist.")
        );

        List<UserStock> stocks = List.of((UserStock) Arrays.stream(Ticker.values()).map(
                ticker -> new UserStock(ticker, 0, user)
        ));

        Portfolio newPortfolio = new Portfolio(name, user, stocks);
        portfolioRespository.save(newPortfolio);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPortfolio);
    }

    public ResponseEntity<Portfolio> renamePortfolio(Long id, String newName, String token){
        String userEmail = jwtUtils.extractUsername(token);
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalStateException("User with email " + userEmail +" does not exist.")
        );

        Portfolio portfolio = portfolioRespository.findById(id)
                .orElseThrow(
                        () -> new IllegalStateException("Portfolio with id: " + id +" does not exist.")
                );

        if (!portfolio.getIsActive()){
            throw new IllegalStateException("" +
                    "Portfolio is deactivated." +
                    "Cannot run operations on this portfolio");
        }

        portfolio.setPortfolioName(newName);

        portfolioRespository.save(portfolio);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(portfolio);
    }

    public ResponseEntity<String> deactivatePortfolio(Long id, String token){
        String userEmail = jwtUtils.extractUsername(token);
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalStateException("User with email " + userEmail +" does not exist.")
        );

        Portfolio portfolio = portfolioRespository.findById(id)
                .orElseThrow(
                        () -> new IllegalStateException("Portfolio with id: " + id +" does not exist.")
                );

        if (!portfolio.getIsActive()){
            throw new IllegalStateException("" +
                    "Portfolio already is deactivated.");
        }

        portfolio.setIsActive(false);
        portfolioRespository.save(portfolio);

        return ResponseEntity.ok("Portfolio deactivated!");
    }
}
