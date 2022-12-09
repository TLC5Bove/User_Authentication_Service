package com.bovetlc.user_authentication_service.entity.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class UserRequest {

    private final String name;
    private final String email;
    private final String password;

    private final String username;
}
