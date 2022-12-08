package com.bovetlc.user_authentication_service.entity.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class GenericRequest {
    private Double amount;
    private String portfolioName;
}
