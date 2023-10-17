package com.example.demo.domain.specification;

import com.example.demo.domain.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class TopLevelSpecifications {
    public static Specification<TopLevelEntity> byFirstActive(Boolean active) {
        if (active == null) {
            return (root, query, criteriaBuilder) -> null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.join(TopLevelEntity_.SUBS, JoinType.INNER)
                        .join(SubEntity_.FIRST, JoinType.INNER).get(FirstEntity_.ACTIVE)
                , active
        );
    }

    public static Specification<TopLevelEntity> bySecondStatus(List<Status> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return (root, query, criteriaBuilder) -> null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(
                root.join(TopLevelEntity_.SUBS, JoinType.INNER).join(SubEntity_.SECONDS, JoinType.INNER).get(SecondEntity_.STATUS)
        ).value(statuses);
    }

    public static Specification<TopLevelEntity> byFirstActiveAndSecondStatus(Boolean active, List<Status> statuses) {
        if (active == null && (statuses == null || statuses.isEmpty() )) {
            return (root, query, criteriaBuilder) -> null;
        }
        return (root, query, criteriaBuilder) -> {
            Join<TopLevelEntity, SubEntity> topSub = root.join(TopLevelEntity_.SUBS);
            Join<SubEntity, FirstEntity> subFirst = topSub.join(SubEntity_.FIRST);
            Join<SubEntity, SecondEntity> subSecond = topSub.join(SubEntity_.SECONDS);

            Predicate predicateActive = criteriaBuilder.equal(subFirst.get(FirstEntity_.ACTIVE), active);
            Predicate predicateStatuses = criteriaBuilder.in(subSecond.get(SecondEntity_.STATUS)).value(statuses);

            if (active == null) {
                return predicateStatuses;
            }
            if(statuses == null || statuses.isEmpty()) {
                return predicateActive;
            }
            return criteriaBuilder.and(predicateActive, predicateStatuses);

        };
    }
}
