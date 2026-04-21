package com.smartseason.modules.field.service;

import com.smartseason.common.enums.FieldStatus;
import com.smartseason.common.enums.FieldStage;
import com.smartseason.modules.field.dto.FieldCreateRequest;
import com.smartseason.modules.field.dto.FieldResponse;
import com.smartseason.modules.field.entity.Field;
import com.smartseason.modules.field.repository.FieldRepository;
import com.smartseason.modules.user.entity.User;
import com.smartseason.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class FieldService {

    private final FieldRepository fieldRepository;

    private final UserRepository userRepository;

    public FieldService(
            FieldRepository fieldRepository, UserRepository userRepository) {
        this.fieldRepository = fieldRepository;
        this.userRepository = userRepository;
    }

    // ---------------------------
    // 1. STATE TRANSITION RULES
    // ---------------------------

    public void validateTransition(FieldStage current, FieldStage next) {
        if (next.ordinal() != current.ordinal() + 1) {
            throw new IllegalStateException("Invalid stage transition");
        }
    }

    public void updateFieldStage(UUID fieldId, FieldStage newFieldStage) {

        Field field = getFieldOrThrow(fieldId);

        validateTransition(field.getFieldStage(), newFieldStage);

        field.setFieldStage(newFieldStage);

        fieldRepository.save(field);
    }

    // ---------------------------
    // 2. STATUS DOMAIN LOGIC
    // ---------------------------

    public FieldStatus computeStatus(Field field) {

        if (field.getFieldStage() == FieldStage.HARVESTED) {
            return FieldStatus.COMPLETED;
        }

        long days = daysSincePlanting(field);

        int expected = expectedDays(field.getFieldStage());

        return (days > expected)
                ? FieldStatus.AT_RISK
                : FieldStatus.ACTIVE;
    }

    private long daysSincePlanting(Field field) {
        return ChronoUnit.DAYS.between(
                field.getPlantingDate(),
                LocalDate.now()
        );
    }

    private int expectedDays(FieldStage fieldStage) {
        return switch (fieldStage) {
            case PLANTED -> 7;
            case GROWING -> 37;
            case READY -> 44;
            default -> 0;
        };
    }

    // ---------------------------
    // 3. CREATE
    // ---------------------------

    public FieldResponse createField(FieldCreateRequest req) {

        Field field = new Field();
        field.setName(req.name);
        field.setCropType(req.cropType);
        field.setPlantingDate(req.plantingDate);
        field.setFieldStage(FieldStage.PLANTED);

        User agent = userRepository.findById(req.agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        field.setAssignedAgent(agent);

        Field saved = fieldRepository.save(field);

        return mapToResponse(saved);
    }

    // ---------------------------
    // 4. QUERY HELPERS
    // ---------------------------

    public Field getFieldOrThrow(UUID id) {
        return fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));
    }

    public FieldResponse getFieldById(UUID id) {
        return mapToResponse(getFieldOrThrow(id));
    }

    // ---------------------------
    // 5. MAPPING (local for now)
    // ---------------------------

    private FieldResponse mapToResponse(Field field) {

        FieldStatus status = computeStatus(field);

        FieldResponse res = new FieldResponse();
        res.id = field.getId();
        res.name = field.getName();
        res.cropType = field.getCropType();
        res.plantingDate = field.getPlantingDate();
        res.fieldStage = field.getFieldStage();
        res.status = status;
        res.assignedAgentId = field.getAssignedAgent().getId();
        return res;
    }

    public List<FieldResponse> getAllFields() {

        return fieldRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


}

