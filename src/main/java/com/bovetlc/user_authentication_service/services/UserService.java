package com.bovetlc.user_authentication_service.services;

import com.bovetlc.user_authentication_service.entity.Admin;
import com.bovetlc.user_authentication_service.entity.User;
import com.bovetlc.user_authentication_service.entity.dto.AdminResponse;
import com.bovetlc.user_authentication_service.entity.dto.UserResponse;
import com.bovetlc.user_authentication_service.repository.AdminRepository;
import com.bovetlc.user_authentication_service.repository.UserRepository;
import com.bovetlc.user_authentication_service.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
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
        boolean emailExists = userRepository.findByEmail(user.getEmail()).isPresent();
        boolean usernameExists = userRepository.findByUsername(user.getUsername()).isPresent();

        if (emailExists){
            throw new IllegalStateException("User with email: " + user.getEmail() + " already exists.");
        }
        if (usernameExists){
            throw new IllegalStateException("User with username: " + user.getUsername() + " already exists.");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        activateUserAccount(user.getEmail());

        return "User Successfully Registered!";
    }

    public String registerAdmin(Admin admin){
        boolean adminEmailExists = adminRepository.findByEmail(admin.getEmail()).isPresent();
        boolean usernameExists = adminRepository.findByUsername(admin.getUsername()).isPresent();

        if (adminEmailExists){
            throw new IllegalStateException("Admin with email: " + admin.getEmail() + " already exists.");
        }
        if (usernameExists){
            throw new IllegalStateException("Admin with username: " + admin.getUsername() + " already exists.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);

        adminRepository.save(admin);

        return "Admin Registered Successfully";
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

    public AdminResponse loginAdmin(String email, String password){
        Admin admin = adminRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Admin with email " + email + " does not exist!")
        );

        boolean matches = bCryptPasswordEncoder.matches(password, admin.getPassword());

        if (!matches){
            throw new IllegalStateException("Admin email or password invalid");
        }
        if (!admin.isEnabled()){
            throw new IllegalStateException("Admin account not activated");
        }

        String jwtToken = jwtUtils.generateToken(admin);

        return new AdminResponse(
                admin.getEmail(),
                admin.getUsername(),
                jwtToken);
    }

    public ResponseEntity<UserResponse> depositAmountIntoUserAccount(Long id, String authToken, Double amount) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User with id: "+ id + " not found")
        );
        String username = jwtUtils.extractUsername(authToken);
        if (!user.getUsername().matches(username)){
            throw new IllegalStateException("Cannot deposit into this account");
        }
        Double balance = user.getBalance();

        user.setBalance(balance + amount);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new UserResponse(
                        user.getName(),
                        user.getEmail(),
                        user.getUsername(),
                        user.getBalance(),
                        "Balance Updated"
                )
        );
    }

    public ResponseEntity<UserResponse> withdrawAmountFromUserAccount(Long id, String authToken, Double amount) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User with id: "+ id + " not found")
        );
        String username = jwtUtils.extractUsername(authToken);
        if (!user.getUsername().matches(username)){
            throw new IllegalStateException("Cannot deposit into this account");
        }
        Double balance = user.getBalance();

        if (balance < amount){
            throw new IllegalStateException("Balance is not enough to withdraw this amount: $" + amount);
        }

        user.setBalance(balance - amount);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new UserResponse(
                        user.getName(),
                        user.getEmail(),
                        user.getUsername(),
                        user.getBalance(),
                        "Balance Updated"
                )
        );
    }
}
