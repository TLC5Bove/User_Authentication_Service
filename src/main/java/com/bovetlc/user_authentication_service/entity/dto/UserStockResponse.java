package com.bovetlc.user_authentication_service.entity.dto;

import com.bovetlc.user_authentication_service.entity.enums.Ticker;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserStockResponse {
    private Ticker product;
    private Integer quantity;
}
