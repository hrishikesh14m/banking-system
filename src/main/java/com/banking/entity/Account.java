package com.banking.entity;

import com.banking.enums.AccountStatus;
import com.banking.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts",
    uniqueConstraints = {
        @UniqueConstraint(
                name="uk_account_number",
                columnNames = "account_number"
        )
    }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account extends BaseEntity{

    @Column(
            name="account_number",
            nullable = false,
            length = 20,
            unique = true
    )
    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(
            name = "customer_id",
            nullable = false
    )
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "account_type",
            nullable = false,
            length = 20
    )
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private AccountStatus accountStatus;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
            //example : 10000000000000000.00
    )
    private BigDecimal balance;
}
