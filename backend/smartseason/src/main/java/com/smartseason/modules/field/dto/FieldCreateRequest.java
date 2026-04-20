package com.smartseason.modules.field.dto;

import java.time.LocalDate;
import java.util.UUID;

public class FieldCreateRequest {
    public String name;
    public String cropType;
    public LocalDate plantingDate;
    public UUID agentId;
}
