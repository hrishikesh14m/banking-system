package com.banking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Username is required.")
    @Size(
            max = 100,
            min = 3,
            message = "Username must be between 3 and 100 characters."
    )
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(
            min = 6,
            max = 100,
            message = "Password must be between 6 and 100 characters."
    )
    private String password;

}
