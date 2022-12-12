package com.bovetlc.user_authentication_service.controller;

import com.bovetlc.user_authentication_service.entity.dto.GenericRequest;
import com.bovetlc.user_authentication_service.entity.dto.PortfolioResponse;
import com.bovetlc.user_authentication_service.entity.dto.UserResponse;
import com.bovetlc.user_authentication_service.entity.enums.Ticker;
import com.bovetlc.user_authentication_service.services.PortfolioService;
import com.bovetlc.user_authentication_service.services.UserService;
import com.bovetlc.user_authentication_service.services.UserStockService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final PortfolioService portfolioService;
    private final UserStockService userStockService;

    @PutMapping("/deposit/{id}")
    public ResponseEntity<UserResponse> depositIntoAccount(
            @PathVariable("id") Long id,
            @RequestHeader HttpHeaders request,
            @RequestBody GenericRequest genericRequest){
        String authHeader = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        return userService.depositAmountIntoUserAccount(id, authHeader, genericRequest.getAmount());
    }

    @PutMapping("/withdraw/{id}")
    public ResponseEntity<UserResponse> withdrawFromAccount(
            @PathVariable("id") Long id,
            @RequestHeader HttpHeaders request,
            @RequestBody GenericRequest genericRequest){
        String authToken = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        return userService.withdrawAmountFromUserAccount(id, authToken, genericRequest.getAmount());
    }

    @PostMapping("/portfolio/new")
    public ResponseEntity<PortfolioResponse> createPortfolio(
            @RequestHeader HttpHeaders request,
            @RequestBody GenericRequest genericRequest
    ){
        String authToken = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        return portfolioService.createNewPortfolio(genericRequest.getPortfolioName(), authToken);
    }

    @PutMapping("/portfolio/{id}")
    public ResponseEntity<PortfolioResponse> renamePortfolio(
            @PathVariable("id") Long id,
            @RequestHeader HttpHeaders request,
            @RequestBody GenericRequest genericRequest
    ){
        String authToken = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        return portfolioService.renamePortfolio(id, genericRequest.getPortfolioName(), authToken);
    }

    @PutMapping("/portfolio/deactivate/{id}")
    public ResponseEntity<String> deactivatePortfolio(
            @PathVariable("id") Long id,
            @RequestHeader HttpHeaders request
    ){
        String authToken = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        return portfolioService.deactivatePortfolio(id, authToken);
    }

    @GetMapping("/portfolio/{id}")
    public ResponseEntity<PortfolioResponse> getPortfolio(
            @PathVariable("id") Long id,
            @RequestHeader HttpHeaders request){
        String authToken = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        System.out.println(authToken);
        return portfolioService.getPortfolioById(id, authToken);
    }

    @GetMapping("/portfolio")
    public ResponseEntity<List<PortfolioResponse>> getAllPortfolios(@RequestHeader HttpHeaders request){
        String authToken = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        return portfolioService.getAllPortfoliosPerUser(authToken);
    }

    @PostMapping("/portfolio/stock/{id}")
    public ResponseEntity<PortfolioResponse> addStockToPortfolio(
            @PathVariable("id") Long id,
            @RequestHeader HttpHeaders request,
            @RequestBody GenericRequest genericRequest){
        String authToken = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        return userStockService.addStockToPortfolio(id, Ticker.valueOf(genericRequest.getTicker()), authToken);
    }

//    @PostMapping("/portfolio/stocks/{id}")
//    public ResponseEntity<PortfolioResponse> addManyStocksToPortfolio(
//            @PathVariable("id") Long id,
//            @RequestHeader HttpHeaders request,
//            @RequestBody GenericRequest genericRequest){
//        String authToken = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
//        List<String> tickers = List.of(genericRequest.getTickerList().split(","));
//        System.out.println(tickers);
//        Set<Ticker> tickerList = new HashSet<>();
//        tickers.forEach(ticker -> {
//            if (!Objects.equals(ticker, "")){
//                tickerList.add(Ticker.valueOf(ticker));
//            }
//        });
//        return userStockService.addManyStocksToPortfolio(id, tickerList, authToken);
//    }

    @PutMapping("/portfolio/stock/{id}")
    public ResponseEntity<PortfolioResponse> updateStockQuantityInPortfolio(
            @PathVariable("id") Long id,
            @RequestHeader HttpHeaders request,
            @RequestBody GenericRequest genericRequest){
        String authToken = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        return userStockService.updateStockQuantityInAPortfolio(
                id,
                authToken,
                genericRequest.getQuantity(),
                Ticker.valueOf(genericRequest.getTicker()));
    }

    @PutMapping("/portfolio/stock/delete/{id}")
    public ResponseEntity<PortfolioResponse> removeStockFromPortfolio(
            @PathVariable("id") Long id,
            @RequestHeader HttpHeaders request,
            @RequestBody GenericRequest genericRequest){
        String authToken = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        return userStockService.removeStockFromAPortfolio(id, authToken, Ticker.valueOf(genericRequest.getTicker()));
    }
}
