package com.smartseason.modules.field.repository;

import com.smartseason.modules.field.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FieldRepository extends JpaRepository<Field, UUID> {
    List<Field> findByAssignedAgentId(UUID agentId);
}
