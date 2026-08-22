package com.banking.dto.response;

import com.banking.enums.AccountStatus;
import com.banking.enums.AccountType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class AccountResponse {

    private Long id;

    private String accountNumber;
    private Long customerId;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus accountStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
