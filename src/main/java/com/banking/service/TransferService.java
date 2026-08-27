package com.banking.service;

import com.banking.dto.request.TransferRequest;
import com.banking.dto.response.BankTransactionResponse;
import com.banking.dto.response.TransferResponse;

public interface TransferService {

    TransferResponse transfer(TransferRequest transferRequest);
}
