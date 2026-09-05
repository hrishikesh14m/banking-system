package com.banking.service.serviceImpl;

import com.banking.dto.request.AccountRequest;
import com.banking.dto.request.MoneyRequest;
import com.banking.dto.response.AccountResponse;
import com.banking.dto.response.BankTransactionResponse;
import com.banking.entity.Account;
import com.banking.entity.BankTransaction;
import com.banking.entity.Customer;
import com.banking.enums.AccountStatus;
import com.banking.enums.CustomerStatus;
import com.banking.enums.TransactionStatus;
import com.banking.enums.TransactionType;
import com.banking.exception.BusinessException;
import com.banking.exception.ResourceNotFoundException;
import com.banking.mapper.AccountMapper;
import com.banking.mapper.BankTransactionMapper;
import com.banking.repository.AccountRepository;
import com.banking.repository.BankTransactionRepository;
import com.banking.repository.CustomerRepository;
import com.banking.service.AccountNumberGenerator;
import com.banking.service.AccountService;
import com.banking.service.TransactionReferenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountNumberGenerator accountNumberGenerator;

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    @Autowired
    private TransactionReferenceGenerator transactionReferenceGenerator;

    @Autowired
    private BankTransactionMapper bankTransactionMapper;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) {

        Customer customer = customerRepository.findById(accountRequest.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found with id: " + accountRequest.getCustomerId())
        );

        if (customer.getCustomerStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("Cannot create account for inactive customer");
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
                () -> new ResourceNotFoundException("Account not found for " + accountNumber)
        );

        return accountMapper.toResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getCustomersAccount(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id : " + customerId);
        }

        return accountRepository.findByCustomerId(customerId).stream()
                .map(accountMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public void closeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNumber));

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessException("cannot close an account with non-zero balance");
        }

        account.setAccountStatus(AccountStatus.CLOSED);

        accountRepository.save(account);
    }

    @Override
    @Transactional
    public BankTransactionResponse deposit(
            String accountNumber,
            MoneyRequest moneyRequest
    ) {
        log.info("Depositing amount against accountNumber: {} ", accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found : " + accountNumber));

        /*
        Validating if account / customer is ACTIVE
         */
        validateAccountForTransaction(account);

        BigDecimal balanceBefore = account.getBalance();

        BigDecimal balanceAfter = balanceBefore.add(moneyRequest.getAmount());

        account.setBalance(balanceAfter);

        Account savedAccount = accountRepository.save(account);

        BankTransaction bankTransaction =
                BankTransaction.builder()
                        .transactionReference(transactionReferenceGenerator.generate())
                        .account(savedAccount)
                        .transactionType(TransactionType.DEPOSIT)
                        .amount(moneyRequest.getAmount())
                        .balanceBefore(balanceBefore)
                        .balanceAfter(balanceAfter)
                        .transactionStatus(TransactionStatus.SUCCESS)
                        .description(moneyRequest.getDescription())
                        .build();

        bankTransactionRepository.save(bankTransaction);

        return bankTransactionMapper.toResponse(bankTransaction);
    }

    @Override
    @Transactional(readOnly = false)
    public BankTransactionResponse withdraw(String accountNumber, MoneyRequest moneyRequest) {

        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new ResourceNotFoundException("Account not found with accountNumber : " + accountNumber)
        );

        validateAccountForTransaction(account);

        //getting balance
        BigDecimal balanceBefore = account.getBalance();

        //checking if requested withdraw is less than available balance
        if (balanceBefore.compareTo(moneyRequest.getAmount()) < 0) {
            throw new BusinessException("Insufficient account balance");
        }

        //substracting balance from presented one
        BigDecimal balanceAfter = balanceBefore.subtract(moneyRequest.getAmount());

        account.setBalance(balanceAfter);


        BankTransaction bankTransaction = BankTransaction.builder()
                .transactionReference(transactionReferenceGenerator.generate())
                .account(account)
                .transactionType(TransactionType.WITHDRAW)
                .amount(moneyRequest.getAmount())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .transactionStatus(TransactionStatus.SUCCESS)
                .description(moneyRequest.getDescription())
                .build();

        BankTransaction savedTransaction = bankTransactionRepository.save(bankTransaction);

        return bankTransactionMapper.toResponse(savedTransaction);
    }


    private void validateAccountForTransaction(Account account) {

        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Cannot Deposit money as accountNumber " + account.getAccountNumber() + " is not ACTIVE.");
        }

        if (account.getCustomer().getCustomerStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("Cannot deposit money as Customer is not ACTIVE.");
        }

    }


}




































