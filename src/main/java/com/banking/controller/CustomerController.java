package com.banking.controller;

import com.banking.dto.request.CustomerRequest;
import com.banking.dto.response.ApiResponse;
import com.banking.dto.response.CustomerResponse;
import com.banking.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CustomerRequest request
    ) {

        CustomerResponse response =
                customerService.createCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<CustomerResponse>builder()
                                .success(true)
                                .message("Customer created successfully")
                                .data(response)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(
            @PathVariable Long id
    ) {

        CustomerResponse response =
                customerService.getCustomerByCustomerId(id);

        return ResponseEntity.ok(
                ApiResponse.<CustomerResponse>builder()
                        .success(true)
                        .message("Customer fetched successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {

        List<CustomerResponse> response =
                customerService.getAllCustomers();

        return ResponseEntity.ok(
                ApiResponse.<List<CustomerResponse>>builder()
                        .success(true)
                        .message("Customers fetched successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request
    ) {

        CustomerResponse response =
                customerService.updateCustomer(id, request);

        return ResponseEntity.ok(
                ApiResponse.<CustomerResponse>builder()
                        .success(true)
                        .message("Customer updated successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateCustomer(
            @PathVariable Long id
    ) {

        customerService.deactivateCustomer(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Customer deactivated successfully")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
