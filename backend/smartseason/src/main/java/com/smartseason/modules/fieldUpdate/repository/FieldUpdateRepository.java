package com.smartseason.modules.fieldUpdate.repository;

import com.smartseason.modules.fieldUpdate.entity.FieldUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FieldUpdateRepository extends JpaRepository<FieldUpdate, UUID> {
    List<FieldUpdate> findByFieldId(UUID fieldId);

}
