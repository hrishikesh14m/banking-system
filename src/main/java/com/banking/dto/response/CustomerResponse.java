package com.banking.dto.response;

import com.banking.enums.CustomerStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private Long id;

    private String name;

    private String email;

    private String phone;

    private CustomerStatus customerStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
