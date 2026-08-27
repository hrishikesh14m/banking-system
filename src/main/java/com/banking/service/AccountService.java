package com.banking.service;

import com.banking.dto.request.AccountRequest;
import com.banking.dto.request.MoneyRequest;
import com.banking.dto.response.AccountResponse;
import com.banking.dto.response.BankTransactionResponse;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);

    AccountResponse getAccount(String accountNumber);

    List<AccountResponse> getCustomersAccount(Long customerId);

    void closeAccount(String accountNumber);

    /**
     * Methods related to the BankTransactions
     */
    BankTransactionResponse deposit(
            String accountNumber,
            MoneyRequest request
    );

    BankTransactionResponse withdraw(
            String accountNumber,
            MoneyRequest moneyRequest
    );

}
