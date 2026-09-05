package com.banking.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionReferenceGenerator {

    public String generate() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 16).toUpperCase();
    }

}
