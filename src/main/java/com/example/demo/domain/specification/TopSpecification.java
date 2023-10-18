package com.example.demo.domain.specification;

import com.example.demo.domain.entity.*;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TopSpecification implements Specification<TopLevelEntity> {

    private final Boolean active;
    private final List<Status> statuses;
    @Override
    public Predicate toPredicate(Root<TopLevelEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Join<TopLevelEntity, SubEntity> topSub = root.join(TopLevelEntity_.SUBS);

        List<Predicate> predicates = new ArrayList<>();
        if (active != null) {
            Join<SubEntity, FirstEntity> subFirst = topSub.join(SubEntity_.FIRST);
            predicates.add(criteriaBuilder.equal(subFirst.get(FirstEntity_.ACTIVE), active));
        }
        if (statuses != null && !statuses.isEmpty()) {
            Join<SubEntity, SecondEntity> subSecond = topSub.join(SubEntity_.SECONDS);
            predicates.add(criteriaBuilder.in(subSecond.get(SecondEntity_.STATUS)).value(statuses));
        }
        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
