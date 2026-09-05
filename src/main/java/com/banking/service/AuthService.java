package com.banking.service;

import com.banking.dto.request.LoginRequest;
import com.banking.dto.request.RegisterRequest;
import com.banking.dto.response.LoginResponse;

public interface AuthService {

    void register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest request);
}
