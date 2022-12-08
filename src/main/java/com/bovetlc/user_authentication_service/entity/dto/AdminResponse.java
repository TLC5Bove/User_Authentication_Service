package com.bovetlc.user_authentication_service.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class AdminResponse {
    private String email;
    private String username;
    private String jwtToken;
}
