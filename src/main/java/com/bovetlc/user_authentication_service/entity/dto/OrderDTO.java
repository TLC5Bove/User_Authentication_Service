package com.bovetlc.user_authentication_service.entity.dto;

import com.bovetlc.user_authentication_service.entity.enums.OrderType;
import com.bovetlc.user_authentication_service.entity.enums.Side;
import com.bovetlc.user_authentication_service.entity.enums.Ticker;
import lombok.*;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Data
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
