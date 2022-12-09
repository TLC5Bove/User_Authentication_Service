package com.bovetlc.user_authentication_service.entity.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class AdminRequest {
    private String email;
    private String username;
    private String password;

}
