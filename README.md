# Spring Data Query with Specifications Problem

This Demo-Project Illustrates the Problem described on [stack-overflow](https://stackoverflow.com/questions/77309162/spring-data-query-with-specifications-prevent-multiple-joins-on-and)

When Listing a Stored Entity (TopEntity) and filtering based on sub-sub entities
(sub.first and sub.second) the sub table is joined twice - wich results in incorrect results.

### Entities

    @Entity
    @Table(name="top")
    public class TopEntity {
        @OneToMany(mappedBy = "top")
        private List<SubEntity> subs;
    }
    
    @Entity
    @Table(name="sub")
    public class SubEntity {
        @ManyToOne
        @JoinColumn(name="top_id", nullable = false)
        private TopEntity top;
    
        @ManyToOne
        @JoinColumn(name="first_id")
        private FirstEntity first;
        
        @OneToMany(mappedBy = "sub")
        private List<SecondEntity> seconds;
    }
    
    @Entity
    @Table(name="first")
    public class FirstEntity {
        private boolean active;
    }
    @Entity
    @Table(name="second")
    public class SecondEntity {
        @ManyToOne
        @JoinColumn(name="sub_id", nullable = false)
        private SubEntity sub;
    
        @Enumerated(EnumType.STRING)
        private Status status;
    }

### Specification

    public static Specification<TopEntity> byFirstActive(Boolean active) {
        if (active == null) {
            return (root, query, criteriaBuilder) -> null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(   
            root.join(TopEntity_.SUBS).join(SubEntity_.FIRST).get(FirstEntity_.ACTIVE)
            , active
        );
    }
    
    public static Specification<TopEntity> bySecondStatus(List<Status> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return (root, query, criteriaBuilder) -> null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(
            root.join(TopEntity_.SUBS).join(SubEntity_.SECONDS).get(SecondEntity_.STATUS)
            ).value(statuses);
    }

### Listing

    public List<TopEntity> listFilterOne(Boolean active, List<Status> statuses) {
        Specification<ToplEntity> filter = byFirstActiveAndSecondStatus(active, statuses);
        return repository.findAll(filter);
    }

### Resulting Query 

    select t1_0.id,t1_0.name from top t1_0
        join sub s1_0 on t1_0.id=s1_0.top_id
        join first f1_0 on f1_0.id=s1_0.first_id
        join sub s2_0 on t1_0.id=s2_0.top_id
        join second s3_0 on s2_0.id=s3_0.sub_id
    where f1_0.active=? and s3_0.status in (?,?)


