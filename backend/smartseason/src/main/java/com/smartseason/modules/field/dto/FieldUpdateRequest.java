package com.smartseason.modules.field.dto;

import com.smartseason.common.enums.FieldStage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldUpdateRequest {
    private FieldStage fieldStage;
    private String notes;
}
