package com.smartseason.modules.fieldUpdate.entity;

import com.smartseason.common.enums.Stage;
import com.smartseason.modules.field.entity.Field;
import com.smartseason.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "field_updates")
@Getter
@Setter
public class FieldUpdate {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Field field;

    @ManyToOne
    private User agent;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    private String notes;

    private LocalDateTime createdAt;
}

