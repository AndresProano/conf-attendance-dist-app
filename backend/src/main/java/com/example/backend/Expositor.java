package com.example.backend;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "expositores")
@Data
public class Expositor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    @Column(length = 1000)
    private String bio;
    private String fotoUrl;
    private String twitter;
    private String linkedin;
    private String github;
}
