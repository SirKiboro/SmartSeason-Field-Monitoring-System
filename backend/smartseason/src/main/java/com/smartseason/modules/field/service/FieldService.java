package com.smartseason.modules.field.service;

import com.smartseason.common.enums.FieldStatus;
import com.smartseason.common.enums.Stage;
import com.smartseason.modules.field.dto.FieldCreateRequest;
import com.smartseason.modules.field.dto.FieldResponse;
import com.smartseason.modules.field.entity.Field;
import com.smartseason.modules.field.repository.FieldRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class FieldService {

    private final FieldRepository fieldRepository;

    public FieldService(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    // ---------------------------
    // 1. STATE TRANSITION RULES
    // ---------------------------

    public void validateTransition(Stage current, Stage next) {
        if (next.ordinal() != current.ordinal() + 1) {
            throw new IllegalStateException("Invalid stage transition");
        }
    }

    public void updateFieldStage(UUID fieldId, Stage newStage) {

        Field field = getFieldOrThrow(fieldId);

        validateTransition(field.getStage(), newStage);

        field.setStage(newStage);

        fieldRepository.save(field);
    }

    // ---------------------------
    // 2. STATUS DOMAIN LOGIC
    // ---------------------------

    public FieldStatus computeStatus(Field field) {

        if (field.getStage() == Stage.HARVESTED) {
            return FieldStatus.COMPLETED;
        }

        long days = daysSincePlanting(field);

        int expected = expectedDays(field.getStage());

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

    private int expectedDays(Stage stage) {
        return switch (stage) {
            case PLANTED -> 7;
            case GROWING -> 37;
            case READY -> 44;
            default -> 0;
        };
    }

    // ---------------------------
    // 3. CREATION FLOW
    // ---------------------------

    public FieldResponse createField(FieldCreateRequest req) {

        Field field = new Field();
        field.setName(req.name);
        field.setCropType(req.cropType);
        field.setPlantingDate(req.plantingDate);
        field.setStage(Stage.PLANTED);

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
    // 5. MAPPING (kept local for now)
    // ---------------------------

    private FieldResponse mapToResponse(Field field) {
        FieldResponse res = new FieldResponse();
        res.id = field.getId();
        res.name = field.getName();
        res.cropType = field.getCropType();
        res.plantingDate = field.getPlantingDate();
        res.stage = field.getStage();
        res.status = computeStatus(field);
        return res;
    }

    public List<FieldResponse> getAllFields() {

        return fieldRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


}

