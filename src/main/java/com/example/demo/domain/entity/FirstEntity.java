package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="first")
@Getter
@Setter
@NoArgsConstructor
public class FirstEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private boolean active;

    public FirstEntity(String name, boolean active) {
        this.name = name;
        this.active = active;
    }
}
