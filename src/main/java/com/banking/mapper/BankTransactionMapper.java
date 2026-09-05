package com.banking.mapper;

import com.banking.dto.response.BankTransactionResponse;
import com.banking.entity.BankTransaction;
import org.springframework.stereotype.Component;

@Component
public class BankTransactionMapper {

    public BankTransactionMapper(){

    }

    public BankTransactionResponse toResponse(BankTransaction bankTransaction){
        return BankTransactionResponse.builder()
                .transactionReference(bankTransaction.getTransactionReference())
                .accountNumber(bankTransaction.getAccount().getAccountNumber())
                .transactionType(bankTransaction.getTransactionType())
                .amount(bankTransaction.getAmount())
                .balanceBefore(bankTransaction.getBalanceBefore())
                .balanceAfter(bankTransaction.getBalanceAfter())
                .transactionStatus(bankTransaction.getTransactionStatus())
                .description(bankTransaction.getDescription())
                .build();
    }

}
