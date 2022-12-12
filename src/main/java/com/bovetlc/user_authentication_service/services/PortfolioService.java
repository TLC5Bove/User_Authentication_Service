package com.bovetlc.user_authentication_service.services;

import com.bovetlc.user_authentication_service.entity.Portfolio;
import com.bovetlc.user_authentication_service.entity.User;
import com.bovetlc.user_authentication_service.entity.UserStock;
import com.bovetlc.user_authentication_service.entity.dto.PortfolioResponse;
import com.bovetlc.user_authentication_service.entity.dto.UserStockResponse;
import com.bovetlc.user_authentication_service.repository.PortfolioRepository;
import com.bovetlc.user_authentication_service.repository.UserRepository;
import com.bovetlc.user_authentication_service.repository.UserStockRepository;
import com.bovetlc.user_authentication_service.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    private final UserStockRepository userStockRepository;

    public String getUsernameFromToken(String token){
        return jwtUtils.extractUsername(token);
    }

    public ResponseEntity<PortfolioResponse> createNewPortfolio(String name, String token){
        String userName = getUsernameFromToken(token);
        User user = userRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalStateException("User with username " + userName +" does not exist.")
        );

        Portfolio newPortfolio = new Portfolio(name, user);
        portfolioRepository.save(newPortfolio);

        List<UserStock> stocks = userStockRepository.findAllByPortfolio(newPortfolio)
                .orElse(new ArrayList<>());

        List<UserStockResponse> stockResponses = new ArrayList<>();
        stocks.forEach(stock -> stockResponses.add(new UserStockResponse(stock.getProduct(), stock.getQuantity())));


        PortfolioResponse response = new PortfolioResponse(
                newPortfolio.getId(),
                newPortfolio.getPortfolioName(),
                newPortfolio.getPortfolioValue(),
                newPortfolio.getIsActive(),
                stockResponses);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<PortfolioResponse> renamePortfolio(Long id, String newName, String token){
        String userName = getUsernameFromToken(token);

        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalStateException("Portfolio with id: " + id +" does not exist.")
                );

        if (!(Objects.equals(portfolio.getUser().getUsername(), userName))){
            throw new IllegalStateException(
                    "You do not own this Portfolio."
            );
        }

        if (!portfolio.getIsActive()){
            throw new IllegalStateException("" +
                    "Portfolio is deactivated.\n" +
                    "Cannot run operations on this portfolio");
        }

        portfolio.setPortfolioName(newName);

        portfolioRepository.save(portfolio);

        List<UserStock> stocks = userStockRepository.findAllByPortfolio(portfolio)
                .orElse(new ArrayList<>());

        List<UserStockResponse> stockResponses = new ArrayList<>();
        stocks.forEach(stock -> stockResponses.add(new UserStockResponse(stock.getProduct(), stock.getQuantity())));


        PortfolioResponse response = new PortfolioResponse(
                portfolio.getId(),
                portfolio.getPortfolioName(),
                portfolio.getPortfolioValue(),
                portfolio.getIsActive(),
                stockResponses);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    public void updatePortfolioValue(Long id, Double value){
        Portfolio port = portfolioRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Portfolio with id: " + id +" does not exist.")
        );

        Double portValue = port.getPortfolioValue();
        port.setPortfolioValue(portValue + value);

        portfolioRepository.save(port);
    }

    public ResponseEntity<String> deactivatePortfolio(Long id, String token){
        String userName = getUsernameFromToken(token);

        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalStateException("Portfolio with id: " + id +" does not exist.")
                );

        if (!(Objects.equals(portfolio.getUser().getUsername(), userName))){
            throw new IllegalStateException(
                    "You do not own this Portfolio."
            );
        }

        if (!portfolio.getIsActive()){
            throw new IllegalStateException("" +
                    "Portfolio already is deactivated.");
        }

        portfolio.setIsActive(false);
        portfolioRepository.save(portfolio);

        return ResponseEntity.ok("Portfolio deactivated!");
    }

    public ResponseEntity<PortfolioResponse> getPortfolioById(Long id, String token) {
        String username = getUsernameFromToken(token);

        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalStateException("Portfolio with id: " + id +" does not exist.")
                );

        if (!(Objects.equals(portfolio.getUser().getUsername(), username))){
            throw new IllegalStateException(
                    "You do not own this Portfolio."
            );
        }

        List<UserStock> stocks = userStockRepository.findAllByPortfolio(portfolio)
                .orElse(new ArrayList<>());

        List<UserStockResponse> stockResponses = new ArrayList<>();
        stocks.forEach(stock -> stockResponses.add(new UserStockResponse(stock.getProduct(), stock.getQuantity())));

        PortfolioResponse portfolioResponse = new PortfolioResponse(
                portfolio.getId(),
                portfolio.getPortfolioName(),
                portfolio.getPortfolioValue(),
                portfolio.getIsActive(),
                stockResponses
        );

        return ResponseEntity.ok(portfolioResponse);
    }

    public ResponseEntity<List<PortfolioResponse>> getAllPortfoliosPerUser(String token) {
        String username = getUsernameFromToken(token);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalStateException("User with username " + username +" does not exist.")
        );

        List<Portfolio> portfolios = portfolioRepository.findAllByUser(user)
                .orElseThrow(
                        () -> new IllegalStateException("Portfolios owned by: " + user.getName() +" does not exist.")
                );

        List<PortfolioResponse> portfolioResponses = new ArrayList<>();

        portfolios.forEach(portfolio -> {
            if (portfolio.getIsActive()){
                List<UserStock> stocks = userStockRepository.findAllByPortfolio(portfolio)
                        .orElse(new ArrayList<>());
                List<UserStockResponse> stockResponses = new ArrayList<>();
                stocks.forEach(stock -> stockResponses.add(new UserStockResponse(stock.getProduct(), stock.getQuantity())));
                portfolioResponses.add(new PortfolioResponse(
                            portfolio.getId(),
                            portfolio.getPortfolioName(),
                            portfolio.getPortfolioValue(),
                            portfolio.getIsActive(),
                            stockResponses));
                }
        });

        return ResponseEntity.ok(portfolioResponses);
    }
}