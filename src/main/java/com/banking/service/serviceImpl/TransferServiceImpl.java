package com.banking.service.serviceImpl;

import com.banking.dto.request.TransferRequest;
import com.banking.dto.response.BankTransactionResponse;
import com.banking.dto.response.TransferResponse;
import com.banking.entity.Account;
import com.banking.entity.BankTransaction;
import com.banking.entity.Customer;
import com.banking.enums.AccountStatus;
import com.banking.enums.CustomerStatus;
import com.banking.enums.TransactionStatus;
import com.banking.enums.TransactionType;
import com.banking.exception.BusinessException;
import com.banking.exception.ResourceNotFoundException;
import com.banking.repository.AccountRepository;
import com.banking.repository.BankTransactionRepository;
import com.banking.service.TransactionReferenceGenerator;
import com.banking.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@Slf4j
public class TransferServiceImpl implements TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    @Autowired
    private TransactionReferenceGenerator transactionReferenceGenerator;

    @Override
    @Transactional
    public TransferResponse transfer(TransferRequest transferRequest) {

        //source and destination cannot be the same
        if (transferRequest.getSourceAccountNumber().equals(transferRequest.getDestinationAccountNumber())) {
            throw new BusinessException("Source and destination cannot be the same.");
        }

        //find the source account
        Account sourceAccount = accountRepository.findByAccountNumber(transferRequest.getSourceAccountNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Source account not found : " + transferRequest.getSourceAccountNumber())
        );

        //find the destination account
        Account destinationAccount = accountRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Destination account not found : " + transferRequest.getDestinationAccountNumber())
        );

        //validate both account source & destination
        validateAccount(sourceAccount);
        validateAccount(destinationAccount);

        //check the sufficient balance
        if (sourceAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new BusinessException("Insufficient balance for transfer");
        }

        //store balances before transfer
        BigDecimal sourceBalanceBefore = sourceAccount.getBalance();
        BigDecimal destinationBalanceBefore = destinationAccount.getBalance();


        //calculate the balance
        BigDecimal sourceBalanceAfter = sourceBalanceBefore.subtract(transferRequest.getAmount());
        BigDecimal destinationBalanceAfter = destinationBalanceBefore.add(transferRequest.getAmount());

        //update the balance
        sourceAccount.setBalance(sourceBalanceAfter);
        destinationAccount.setBalance(destinationBalanceAfter);

        String transferReference = transactionReferenceGenerator.generate();

        //create debit transaction
        BankTransaction debitTransaction = BankTransaction
                .builder()
                .transactionReference(transferReference)
                .account(sourceAccount)
                .amount(transferRequest.getAmount())
                .transactionType(TransactionType.TRANSFER_DEBIT)
                .balanceBefore(sourceBalanceBefore)
                .balanceAfter(sourceBalanceAfter)
                .transactionStatus(TransactionStatus.SUCCESS)
                .description(transferRequest.getDescription())
                .build();

        //create credit transaction
        BankTransaction creditTransaction =
                BankTransaction.builder()
                        .transactionReference(transferReference)
                        .account(destinationAccount)
                        .transactionType(TransactionType.TRANSFER_CREDIT)
                        .amount(transferRequest.getAmount())
                        .balanceBefore(destinationBalanceBefore)
                        .balanceAfter(destinationBalanceAfter)
                        .transactionStatus(TransactionStatus.SUCCESS)
                        .description(transferRequest.getDescription())
                        .build();

        bankTransactionRepository.save(debitTransaction);
        bankTransactionRepository.save(creditTransaction);

        return TransferResponse.builder()
                .transactionReference(transferReference)
                .sourceAccountNumber(sourceAccount.getAccountNumber())
                .destinationAccountNumber(destinationAccount.getAccountNumber())
                .amount(transferRequest.getAmount())
                .sourceBalanceAfter(sourceBalanceAfter)
                .destinationBalanceAfter(destinationBalanceAfter)
                .description(transferRequest.getDescription())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private void validateAccount(Account account) {
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Account is not active : " + account.getAccountNumber());
        }

        if (account.getCustomer().getCustomerStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("Customer is not ACTIVE for account: " + account.getAccountNumber());
        }
    }
}
