package com.bovetlc.user_authentication_service.entity.dto;

import lombok.*;

@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class AdminRequest {
    private String email;
    private String username;
    private String password;

}
