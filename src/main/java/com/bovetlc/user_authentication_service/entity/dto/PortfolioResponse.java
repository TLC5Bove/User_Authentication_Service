package com.bovetlc.user_authentication_service.entity.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class PortfolioResponse {
    Long portfolioId;
    String portfolioName;
    Double portfolioValue;
    Boolean isActive;
    List<UserStockResponse> stocks;
}
