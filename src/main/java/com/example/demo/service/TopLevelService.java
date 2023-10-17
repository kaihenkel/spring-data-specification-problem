package com.example.demo.service;

import com.example.demo.domain.entity.Status;
import com.example.demo.domain.entity.TopLevelEntity;
import com.example.demo.domain.repository.TopLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.domain.specification.TopLevelSpecifications.byFirstActive;
import static com.example.demo.domain.specification.TopLevelSpecifications.bySecondStatus;

@Service
@RequiredArgsConstructor
public class TopLevelService {
    private final TopLevelRepository repository;

    public List<TopLevelEntity> list(Boolean active, List<Status> statuses) {
        Specification<TopLevelEntity> filter = byFirstActive(active).and(bySecondStatus(statuses));
        return repository.findAll(filter);
    }

}
