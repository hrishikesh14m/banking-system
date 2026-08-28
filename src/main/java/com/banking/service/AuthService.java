package com.banking.service;

import com.banking.dto.request.LoginRequest;
import com.banking.dto.request.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    void login(LoginRequest request);
}
