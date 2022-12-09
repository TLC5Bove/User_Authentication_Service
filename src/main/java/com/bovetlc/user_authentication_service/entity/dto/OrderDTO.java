package com.bovetlc.user_authentication_service.entity.dto;

import com.bovetlc.user_authentication_service.entity.enums.OrderType;
import com.bovetlc.user_authentication_service.entity.enums.Side;
import com.bovetlc.user_authentication_service.entity.enums.Ticker;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class OrderDTO {
    private Ticker product;
    private Integer quantity;
    private Double price;
    private Side side;
    private OrderType type;
    private Long userId;
    private Long portId;
    private String OSID;
}
