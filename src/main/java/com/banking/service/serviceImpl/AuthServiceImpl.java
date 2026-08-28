package com.banking.service.serviceImpl;

import com.banking.dto.request.LoginRequest;
import com.banking.dto.request.RegisterRequest;
import com.banking.entity.User;
import com.banking.exception.AuthenticationFailedException;
import com.banking.exception.BusinessException;
import com.banking.repository.UserRepository;
import com.banking.security.enums.Role;
import com.banking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void register(RegisterRequest request) {

        //check if username is already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username is already exists");
        }


        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .enabled(true)
                .build();

        userRepository.save(user);
    }

    @Override
    public void login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException exception) {
            throw new AuthenticationFailedException("Invalid username or password");
        }
    }

}
