package com.example.transacciones.models.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Users")
public class UserEntity {

    @Id @NotNull @GeneratedValue(strategy = GenerationType.AUTO) private Long id;

    @Column(name = "name") @NotNull private String name;

    @Column(name = "email") @NotNull @Email private String email;

    @Column(name = "password") private String password;

    @Column(name = "cedula") private String cedula;

    @Column(name = "balance") private BigDecimal balance;

    @Column(name = "accountNumber") private String accountNumber;

    @Column(name = "is_enable") private boolean isEnable;

    @Column(name = "account_no_expired") private boolean accountNoExpired;

    @Column(name = "account_no_locked") private boolean accountNoLocked;

    @Column(name = "credential_no_expired") private boolean credentialNoExpired;

    @CreationTimestamp
    @Column(name = "createdAt") private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt") private Timestamp updatedAt;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles=new HashSet<>();
}
