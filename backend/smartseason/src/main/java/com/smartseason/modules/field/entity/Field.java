package com.smartseason.modules.field.entity;

import com.smartseason.common.enums.Stage;
import com.smartseason.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fields")
@Getter
@Setter
public class Field {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String cropType;

    private LocalDate plantingDate;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    @ManyToOne
    @JoinColumn(name = "assigned_agent_id")
    private User assignedAgent;

    private LocalDateTime createdAt;
}
