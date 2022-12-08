package com.bovetlc.user_authentication_service.services;

import com.bovetlc.user_authentication_service.entity.User;
import com.bovetlc.user_authentication_service.entity.UserResponse;
import com.bovetlc.user_authentication_service.repository.UserRepository;
import com.bovetlc.user_authentication_service.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {

    // TODO: 1. Register User - Admin
    // TODO: 2. User Sign in - Admin
    // TODO: 3. Deposit into account or Withdraw from account: Update balance +
    // TODO: 4. Deactivate account

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with username: " + username + " not found")
                );
    }
    // enable the user
    public void activateUserAccount(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("user does not exist"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public String registerUser(User user){
        boolean alreadyExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if (alreadyExists){
            throw new IllegalStateException("User with email: " + user.getEmail() + " already exists.");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        activateUserAccount(user.getEmail());

        return UUID.randomUUID().toString();
    }

    public UserResponse loginUser(String email, String password){
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " does not exist!")
        );

        boolean matches = bCryptPasswordEncoder.matches(password, user.getPassword());

        if (!matches){
            throw new IllegalStateException("User email or password invalid");
        }
        if (!user.isEnabled()){
            throw new IllegalStateException("User account not activated");
        }

        String jwtToken = jwtUtils.generateToken(user);

        return new UserResponse(
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getBalance(),
                jwtToken
        );
    }
}
