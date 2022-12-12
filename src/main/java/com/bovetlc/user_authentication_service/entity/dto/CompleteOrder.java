package com.bovetlc.user_authentication_service.entity.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Data
public class CompleteOrder {
    private String OSID;
    private Double cummPrice;
}
