package com.banking.dto.request;

import com.banking.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequest {

    @NotNull(message = "Customer ID is required.")
    private Long customerId;

    @NotNull(message = "Account type is required.")
    private AccountType accountType;

}
