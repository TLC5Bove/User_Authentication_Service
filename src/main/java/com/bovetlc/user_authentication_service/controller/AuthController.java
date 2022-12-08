package com.bovetlc.user_authentication_service.controller;

import com.bovetlc.user_authentication_service.entity.dto.AdminRequest;
import com.bovetlc.user_authentication_service.entity.dto.AdminResponse;
import com.bovetlc.user_authentication_service.entity.dto.UserRequest;
import com.bovetlc.user_authentication_service.entity.dto.UserResponse;
import com.bovetlc.user_authentication_service.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    // TODO: 1. Post mapping: Register: Admin
    // TODO: 2. Post mapping: Log in - Admin

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userRequest));
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminRequest adminRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.adminRegister(adminRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> userLogin(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(authService.userLogin(userRequest));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AdminResponse> adminLogin(@RequestBody AdminRequest adminRequest){
        return ResponseEntity.ok(authService.adminLogin(adminRequest));
    }
}
