package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="second")
@Getter
@Setter
@NoArgsConstructor
public class SecondEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name="sub_id", nullable = false)
    private SubEntity sub;
    private String name;
    @Enumerated(EnumType.STRING)
    private Status status;

    public SecondEntity(SubEntity sub, String name, Status status) {
        this.sub = sub;
        this.name = name;
        this.status = status;
    }
}
