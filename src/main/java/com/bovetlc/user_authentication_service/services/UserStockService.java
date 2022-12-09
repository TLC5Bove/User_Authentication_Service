package com.bovetlc.user_authentication_service.services;

import com.bovetlc.user_authentication_service.entity.Portfolio;
import com.bovetlc.user_authentication_service.entity.User;
import com.bovetlc.user_authentication_service.entity.UserStock;
import com.bovetlc.user_authentication_service.entity.dto.PortfolioResponse;
import com.bovetlc.user_authentication_service.entity.enums.Ticker;
import com.bovetlc.user_authentication_service.repository.PortfolioRepository;
import com.bovetlc.user_authentication_service.repository.UserRepository;
import com.bovetlc.user_authentication_service.repository.UserStockRepository;
import com.bovetlc.user_authentication_service.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserStockService {
    private final PortfolioService portfolioService;
    private final UserStockRepository userStockRepository;
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    private String getUsernameFromToken(String token){
        return jwtUtils.extractUsername(token);
    }

    public ResponseEntity<PortfolioResponse> addStockToPortfolio(Long id, Ticker ticker, String authToken){
        String username = getUsernameFromToken(authToken);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalStateException("" +
                        "User with username "+ username + " " +
                        "does not exist.")
        );

        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("" +
                        "Portfolio with id: " + id + " " +
                        "does not exist")
        );

        if (!username.equals(portfolio.getUser().getUsername())){
            throw new IllegalStateException("" +
                    "User does not own this portfolio.");
        }

        UserStock userStocks = userStockRepository.findAllByPortfolio(portfolio).orElse(new ArrayList<>())
                .stream().filter(stock -> stock.getProduct() == ticker).findFirst().orElse(null);

        if (userStocks == null){
            UserStock userStock = new UserStock(
                    ticker,
                    0,
                    user,
                    portfolio
            );

            userStockRepository.save(userStock);
        }

        return portfolioService.getPortfolioById(id, authToken);
    }

//    public ResponseEntity<PortfolioResponse> addManyStocksToPortfolio(Long id, Set<Ticker> tickers, String authToken){
//        String username = getUsernameFromToken(authToken);
//        User user = userRepository.findByUsername(username).orElseThrow(
//                () -> new IllegalStateException("" +
//                        "User with username "+ username + " " +
//                        "does not exist.")
//        );
//
//        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(
//                () -> new IllegalStateException("" +
//                        "Portfolio with id: " + id + " " +
//                        "does not exist")
//        );
//
//        if (!username.equals(portfolio.getUser().getUsername())){
//            throw new IllegalStateException("" +
//                    "User does not own this portfolio.");
//        }
//
//        List<UserStock> stocks = userStockRepository.findAllByPortfolio(portfolio).orElse(new ArrayList<>());
//
//        if(stocks.size() == 0){
//            tickers.forEach(
//                    ticker -> {
//                        stocks.add(new UserStock(ticker, 0, user, portfolio));
//                    }
//            );
//
//            userStockRepository.saveAll(stocks);
//        }
//
//        Set<Ticker> stockTickers = new HashSet<>();
//        stocks.forEach(stock -> stockTickers.add(stock.getProduct()));
//
//        List<UserStock> newStocks = new ArrayList<>();
//
//        tickers.forEach(
//                ticker -> {
//                    if (!stockTickers.contains(ticker)){
//                        newStocks.add(new UserStock(ticker, 0, user, portfolio));
//                    }
//                }
//        );
//
//        userStockRepository.saveAll(newStocks);
//
//        return portfolioService.getPortfolioById(id, authToken);
//    }

    public ResponseEntity<PortfolioResponse> updateStockQuantityInAPortfolio(Long id, String authToken, Integer quantity, Ticker ticker){
        String username = getUsernameFromToken(authToken);

        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("" +
                        "Portfolio with id: " + id + " " +
                        "does not exist")
        );

        if (!username.equals(portfolio.getUser().getUsername())){
            throw new IllegalStateException("" +
                    "User does not own this portfolio.");
        }

        List<UserStock> userStocks = userStockRepository.findAllByPortfolio(portfolio).orElse(new ArrayList<>());

        if (userStocks.size() == 0){
            throw new IllegalStateException("This portfolio is empty");
        }

        UserStock stock = userStocks.stream().filter(
                userStock -> userStock.getProduct() == ticker
        ).findFirst().orElse(null);

        if (stock ==null){
            throw new IllegalStateException("This portfolio does not have this stock");
        }

        int stockQuantity = stock.getQuantity();
        stock.setQuantity(stockQuantity+quantity);

        userStockRepository.save(stock);

        return portfolioService.getPortfolioById(id, authToken);
    }

    public ResponseEntity<PortfolioResponse> removeStockFromAPortfolio(Long id, String authToken, Ticker ticker){
        String username = getUsernameFromToken(authToken);

        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("" +
                        "Portfolio with id: " + id + " " +
                        "does not exist")
        );

        if (!username.equals(portfolio.getUser().getUsername())){
            throw new IllegalStateException("" +
                    "User does not own this portfolio.");
        }

        List<UserStock> userStocks = userStockRepository.findAllByPortfolio(portfolio).orElse(new ArrayList<>());

        if (userStocks.size() == 0){
            throw new IllegalStateException("This portfolio is empty");
        }
        UserStock stock = userStocks.stream().filter(x -> x.getProduct() == ticker).findFirst().orElse(null);

        if (stock == null || stock.getQuantity() != 0){
            throw new IllegalStateException("" +
                    "Sell all stocks before you can remove it from this portfolio");
        }
        userStockRepository.delete(stock);

        return portfolioService.getPortfolioById(id, authToken);
    }
}
