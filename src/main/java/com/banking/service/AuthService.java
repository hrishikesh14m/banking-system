package com.banking.service;

import com.banking.dto.request.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);
}
