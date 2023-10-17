package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="sub")
@Getter
@Setter
@NoArgsConstructor
public class SubEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name="top_id", nullable = false)
    private TopLevelEntity top;
    private String name;
    @ManyToOne
    @JoinColumn(name="first_id")
    private FirstEntity first;

    @OneToMany(mappedBy = "sub")
    private List<SecondEntity> seconds;

    public SubEntity(String name, FirstEntity first, List<SecondEntity> seconds) {
        this.name = name;
        this.first = first;
        this.seconds = seconds;
    }
}
