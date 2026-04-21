package com.smartseason.modules.user.entity;

import com.smartseason.common.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password; //Hashed

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;
}
