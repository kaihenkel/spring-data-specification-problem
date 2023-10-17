package com.example.demo.domain.specification;

import com.example.demo.domain.entity.*;
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
                root.join(TopLevelEntity_.SUBS).join(SubEntity_.FIRST).get(FirstEntity_.ACTIVE)
                , active
        );
    }

    public static Specification<TopLevelEntity> bySecondStatus(List<Status> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return (root, query, criteriaBuilder) -> null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(
                root.join(TopLevelEntity_.SUBS).join(SubEntity_.SECONDS).get(SecondEntity_.STATUS)
        ).value(statuses);
    }
}
