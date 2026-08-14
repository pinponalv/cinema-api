package com.example.cinema_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class RefreshToken {

    @Id
    private String id;

    @ManyToOne(nullable = false, name = "user_id")
    private UserSec userSec;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    @Column(nullable = false)
    private boolean revoked = false;
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
