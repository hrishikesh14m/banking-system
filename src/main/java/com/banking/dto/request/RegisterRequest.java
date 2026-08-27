package com.banking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(
            min = 3,
            max = 100,
            message = "Username must be between 3 and 100 characters"
    )
    private String username;

    @NotBlank(message = "Password is required")
    @Size(
            min = 6,
            max = 100,
            message = "password must be between 6 and 100 characters"
    )
    private String password;

}
