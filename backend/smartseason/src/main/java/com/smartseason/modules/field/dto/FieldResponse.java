package com.smartseason.modules.field.dto;

import com.smartseason.common.enums.FieldStatus;
import com.smartseason.common.enums.FieldStage;
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
    public FieldStage fieldStage;
    public FieldStatus status;
    public UUID assignedAgentId;

    public FieldResponse() {

    }

}
