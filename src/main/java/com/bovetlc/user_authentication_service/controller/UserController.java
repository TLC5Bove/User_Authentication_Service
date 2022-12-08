package com.bovetlc.user_authentication_service.controller;

import com.bovetlc.user_authentication_service.entity.Portfolio;
import com.bovetlc.user_authentication_service.entity.dto.GenericRequest;
import com.bovetlc.user_authentication_service.entity.dto.UserResponse;
import com.bovetlc.user_authentication_service.services.PortfolioService;
import com.bovetlc.user_authentication_service.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {
    // TODO: 1. Create Portfolio - test
    // TODO: 2. UPDATE PORTFOLIO - test
    // TODO: 3. DEACTIVATE PORTFOLIO - test
    private final UserService userService;
    private final PortfolioService portfolioService;

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
    public ResponseEntity<Portfolio> createPortfolio(
            @RequestHeader HttpHeaders request,
            @RequestBody GenericRequest genericRequest
    ){
        String authToken = Objects.requireNonNull(request.getFirst(AUTHORIZATION)).substring(7);
        return portfolioService.createNewPortfolio(genericRequest.getPortfolioName(), authToken);
    }

    @PutMapping("/portfolio/{id}")
    public ResponseEntity<Portfolio> renamePortfolio(
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
}
