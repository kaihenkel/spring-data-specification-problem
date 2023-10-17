package com.example.demo.service;

import com.example.demo.domain.entity.*;
import com.example.demo.domain.repository.TopLevelRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TopLevelServiceTest {

    @Autowired
    private TopLevelRepository repository;

    @Autowired
    private TopLevelService service;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    private void setup() {
        transactionTemplate.executeWithoutResult(status -> {
            if (repository.count() == 0) {
                    TopLevelEntity a = repository.save(new TopLevelEntity("a"));
                    sub(a,"a-1", true, Status.ACTIVE, Status.PENDING);

                    TopLevelEntity b = repository.save(new TopLevelEntity("a"));
                    sub(a,"b-1", false, Status.PENDING, Status.DISABLED);
                }
            }
        );
    }

    private SubEntity sub(TopLevelEntity top, String name, boolean firstActive, Status... secondStatuses) {
        FirstEntity first = new FirstEntity(name + "-" + firstActive, firstActive);
        entityManager.persist(first);

        SubEntity sub = new SubEntity().setName(name);
        sub.setTop(top);
        sub.setFirst(first);
        entityManager.persist(sub);

        sub.setSeconds(
        Stream.of(secondStatuses)
                .map(status -> new SecondEntity(sub, name + "-" + status, status))
                .peek(entityManager::persist)
                .toList());
        return sub;
    }

    @Nested
    class SeparateSpecifications {
        @Test
        void testListAll() {
            List<TopLevelEntity> actual = service.list(null, null);
            assertEquals(2, actual.size());
        }

        @Test
        void testListActive() {
            List<TopLevelEntity> actual = service.list(true, null);
            assertEquals(1, actual.size());
        }

        @Test
        void testListDisabled() {
            List<TopLevelEntity> actual = service.list(null, List.of(Status.DISABLED));
            assertEquals(1, actual.size());
        }

        @Test
        void testListActiveDisabled() {
            List<TopLevelEntity> actual = service.list(true, List.of(Status.DISABLED));
            assertEquals(0, actual.size());
        }
    }
    @Nested
    class OneSpecification {
        @Test
        void testListAll() {
            List<TopLevelEntity> actual = service.listFilterOne(null, null);
            assertEquals(2, actual.size());
        }

        @Test
        void testListActive() {
            List<TopLevelEntity> actual = service.listFilterOne(true, null);
            assertEquals(1, actual.size());
        }

        @Test
        void testListDisabled() {
            List<TopLevelEntity> actual = service.listFilterOne(null, List.of(Status.DISABLED));
            assertEquals(1, actual.size());
        }

        @Test
        void testListActiveDisabled() {
            List<TopLevelEntity> actual = service.listFilterOne(true, List.of(Status.DISABLED));
            assertEquals(0, actual.size());
        }
    }
}