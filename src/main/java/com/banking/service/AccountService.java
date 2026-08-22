package com.banking.service;

import com.banking.dto.request.AccountRequest;
import com.banking.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);

    AccountResponse getAccount(String accountNumber);

    List<AccountResponse> getCustomersAccount(Long customerId);

    void closeAccount(String accountNumber);

}
