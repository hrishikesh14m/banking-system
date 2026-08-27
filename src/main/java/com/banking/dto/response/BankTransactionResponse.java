package com.banking.dto.response;

import com.banking.enums.TransactionStatus;
import com.banking.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class BankTransactionResponse {

    private String transactionReference;
    private String accountNumber;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private TransactionStatus transactionStatus;
    private String description;

}
