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
import java.util.UUID;

@Service
public class FieldService {

    private final FieldRepository fieldRepository;

    public FieldService(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    public FieldStatus computeStatus(Field field){
        if (field.getStage() == Stage.HARVESTED) {
            return FieldStatus.COMPLETED;
        }

        long days = ChronoUnit.DAYS.between(
                field.getPlantingDate(),
                LocalDate.now()
        );

        int expected = switch (field.getStage()) {
            case PLANTED -> 7;
            case GROWING -> 37;
            case READY -> 44;
            default -> 0;
        };

        return (days > expected)
                ? FieldStatus.AT_RISK
                : FieldStatus.ACTIVE;

    }

    public void validateTransition(Stage current, Stage next) {
        if (next.ordinal() != current.ordinal() + 1) {
            throw new RuntimeException("Invalid stage transition");
        }
    }

    public void updateFieldStage(UUID fieldId, Stage newStage) {

        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new RuntimeException("Field not found"));

        validateTransition(field.getStage(), newStage);

        field.setStage(newStage);

        fieldRepository.save(field);
    }

    public FieldResponse createField (FieldCreateRequest req){

        Field field = new Field();
        field.setName(req.name);
        field.setCropType(req.cropType);
        field.setPlantingDate(req.plantingDate);
        field.setStage(Stage.PLANTED);

        Field saved = fieldRepository.save(field);

        FieldResponse res = new FieldResponse();
        res.id = saved.getId();
        res.name = saved.getName();
        res.cropType = saved.getCropType();
        res.plantingDate = saved.getPlantingDate();
        res.stage = saved.getStage();
        res.status = computeStatus(saved);

        return res;

    }
}
