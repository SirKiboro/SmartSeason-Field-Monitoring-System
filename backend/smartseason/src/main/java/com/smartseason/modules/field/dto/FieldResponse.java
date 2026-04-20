package com.smartseason.modules.field.dto;

import com.smartseason.common.enums.FieldStatus;
import com.smartseason.common.enums.Stage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FieldResponse {
    public UUID id;
    public String name;
    public String cropType;
    public LocalDate plantingDate;
    public Stage stage;
    public FieldStatus status;

    public FieldResponse() {

    }
}
