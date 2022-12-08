package com.bovetlc.user_authentication_service.services;

import com.bovetlc.user_authentication_service.entity.Admin;
import com.bovetlc.user_authentication_service.entity.User;
import com.bovetlc.user_authentication_service.entity.dto.AdminRequest;
import com.bovetlc.user_authentication_service.entity.dto.AdminResponse;
import com.bovetlc.user_authentication_service.entity.dto.UserRequest;
import com.bovetlc.user_authentication_service.entity.dto.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;

    private boolean isValidEmail(String email){

        String regex = "^(.+)@(\\S+)$";
        final Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
    public String registerUser(UserRequest request){
        boolean isValidEmail = isValidEmail(request.getEmail());

        if (!isValidEmail){
            throw new IllegalStateException("invalid email");
        }
        return userService.registerUser(
                new User(
                        request.getName(),
                        request.getEmail(),
                        request.getPassword(),
                        request.getUsername()
                )
        );
    }

    public String adminRegister(AdminRequest adminRequest){
        boolean isValidEmail  = isValidEmail(adminRequest.getEmail());

        if (!isValidEmail){
            throw new IllegalStateException("invalid email");
        }
        return userService.registerAdmin(
                new Admin(
                        adminRequest.getEmail(),
                        adminRequest.getUsername(),
                        adminRequest.getPassword()
                )
        );
    }

    public UserResponse userLogin(UserRequest request){
        return userService.loginUser(request.getEmail(), request.getPassword());
    }

    public AdminResponse adminLogin(AdminRequest adminRequest){
        return userService.loginAdmin(adminRequest.getEmail(), adminRequest.getPassword());
    }
}
