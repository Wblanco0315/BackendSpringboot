package com.example.transacciones.models.users;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "token", length = 20000)
    private String token;

    @Column(name = "is_logged_out")
    private boolean loggedOut;
}
