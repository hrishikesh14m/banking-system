package com.banking.controller;

import com.banking.dto.request.AccountRequest;
import com.banking.dto.response.AccountResponse;
import com.banking.dto.response.ApiResponse;
import com.banking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @Valid @RequestBody AccountRequest request
    ) {

        AccountResponse response =
                accountService.createAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<AccountResponse>builder()
                                .success(true)
                                .message("Account created successfully")
                                .data(response)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(
            @PathVariable String accountNumber
    ) {

        AccountResponse response =
                accountService.getAccount(accountNumber);

        return ResponseEntity.ok(
                ApiResponse.<AccountResponse>builder()
                        .success(true)
                        .message("Account fetched successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<AccountResponse>>>
    getCustomerAccounts(
            @PathVariable Long customerId
    ) {

        List<AccountResponse> response =
                accountService.getCustomersAccount(customerId);

        return ResponseEntity.ok(
                ApiResponse.<List<AccountResponse>>builder()
                        .success(true)
                        .message("Customer accounts fetched successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/{accountNumber}/close")
    public ResponseEntity<ApiResponse<Void>> closeAccount(
            @PathVariable String accountNumber
    ) {

        accountService.closeAccount(accountNumber);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Account closed successfully")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}
