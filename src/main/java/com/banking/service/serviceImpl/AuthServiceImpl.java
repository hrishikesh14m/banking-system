package com.banking.service.serviceImpl;

import com.banking.dto.request.LoginRequest;
import com.banking.dto.request.RegisterRequest;
import com.banking.dto.response.LoginResponse;
import com.banking.entity.User;
import com.banking.enums.Role;
import com.banking.exception.AuthenticationFailedException;
import com.banking.exception.BusinessException;
import com.banking.repository.UserRepository;
import com.banking.security.jwt.JwtService;
import com.banking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Override
    public void register(RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BusinessException("Username is already exists.");
        }

        //Create user
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(
                        passwordEncoder.encode(registerRequest.getPassword())
                )
                .role(Role.CUSTOMER)
                .enabled(true)
                .build();

        //Save User
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    ));

            //Generating token after authentication.
            String token = jwtService.generateToken(request.getUsername());

            return LoginResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .build();

        }catch (BadCredentialsException exception){
            throw new AuthenticationFailedException("Invalid Username or password");
        }
    }
}
