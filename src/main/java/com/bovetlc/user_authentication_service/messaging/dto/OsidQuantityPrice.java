package com.bovetlc.user_authentication_service.messaging.dto;

import lombok.Data;

@Data
public class OsidQuantityPrice {
    private String osId;
    private Integer quantity;
    private Double price;
}
