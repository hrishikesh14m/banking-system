package com.banking.controller;

import com.banking.dto.request.TransferRequest;
import com.banking.dto.response.ApiResponse;
import com.banking.dto.response.TransferResponse;
import com.banking.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransferResponse>> transfer(
            @Valid @RequestBody TransferRequest request
    ) {

        TransferResponse response =
                transferService.transfer(request);

        return ResponseEntity.ok(
                ApiResponse.<TransferResponse>builder()
                        .success(true)
                        .message("Money transferred successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}
