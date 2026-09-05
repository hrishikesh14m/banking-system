package com.banking.entity;

import com.banking.enums.TransactionStatus;
import com.banking.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table(
        name = "bank_transactions",
        indexes = {
                @Index(
                        name = "idx_transaction_account",
                        columnList = "account_id"
                ),
                @Index(
                        name = "idx_transaction_reference",
                        columnList = "transaction_reference"
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankTransaction extends BaseEntity {

    @Column(
            name = "transaction_reference",
            nullable = false,
            length = 50
    )
    private String transactionReference;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "account_id",
            nullable = false
    )
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "transaction_type",
            nullable = false,
            length = 20
    )
    private TransactionType transactionType;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal amount;


    @Column(
            name = "balance_before",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal balanceBefore;

    @Column(
            name = "balance_after",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal balanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private TransactionStatus transactionStatus;

    @Column(length = 500)
    private String description;

}
