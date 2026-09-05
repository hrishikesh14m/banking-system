package com.banking.entity;

import com.banking.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_customer_email",
                        columnNames = "email"
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer extends BaseEntity{

    @Column(nullable = false,length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String email;

    @Column( unique = true, nullable = false , length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private CustomerStatus customerStatus;

}
