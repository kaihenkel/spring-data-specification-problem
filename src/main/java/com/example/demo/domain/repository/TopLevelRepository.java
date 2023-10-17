package com.example.demo.domain.repository;

import com.example.demo.domain.entity.TopLevelEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TopLevelRepository extends CrudRepository<TopLevelEntity, UUID>, JpaSpecificationExecutor<TopLevelEntity> {
}
