package com.banking.service.serviceImpl;

import com.banking.dto.request.AccountRequest;
import com.banking.dto.response.AccountResponse;
import com.banking.entity.Account;
import com.banking.entity.Customer;
import com.banking.enums.AccountStatus;
import com.banking.enums.CustomerStatus;
import com.banking.mapper.AccountMapper;
import com.banking.repository.AccountRepository;
import com.banking.repository.CustomerRepository;
import com.banking.service.AccountNumberGenerator;
import com.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountNumberGenerator accountNumberGenerator;


    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) {

        Customer customer = customerRepository.findById(accountRequest.getCustomerId()).orElseThrow(
                () -> new RuntimeException("CUstomer not found with id: " + accountRequest.getCustomerId())
        );

        if (customer.getCustomerStatus() != CustomerStatus.ACTIVE) {
            throw new RuntimeException("Cannot create account for inactive customer");
        }

        String accountNumber = accountNumberGenerator.generate();


        Account account = Account.builder()
                .accountNumber(accountNumber)
                .customer(customer)
                .accountType(accountRequest.getAccountType())
                .balance(BigDecimal.ZERO)
                .accountStatus(AccountStatus.ACTIVE)
                .build();

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponse(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new RuntimeException("Account not found for " + accountNumber)
        );

        return accountMapper.toResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getCustomersAccount(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("Customer not found with id : " + customerId);
        }

        return accountRepository.findByCustomerId(customerId).stream()
                .map(accountMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public void closeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("cannot close an account with non-zero balance");
        }

        account.setAccountStatus(AccountStatus.CLOSED);

        accountRepository.save(account);
    }
}
