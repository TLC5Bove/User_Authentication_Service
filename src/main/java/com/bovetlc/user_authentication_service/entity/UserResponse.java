package com.bovetlc.user_authentication_service.entity;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserResponse {
    private String name;
    private String email;
    private String username;
    private Double balance;
    private String jwtToken;
}
