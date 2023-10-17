package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="top")
@Getter
@Setter
@NoArgsConstructor
public class TopLevelEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(mappedBy = "top")
    private List<SubEntity> subs;
    private String name;

    public TopLevelEntity( String name) {
        this.name = name;
    }
}
