package com.banking.entity;

import com.banking.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(
                        name = "idx_user_username",
                        columnList = "username"
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String username;

    @Column(
            nullable = false
    )
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private Role role;

    @Column(nullable = false)
    private boolean enabled;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            unique = true
    )
    private Customer customer;
}
