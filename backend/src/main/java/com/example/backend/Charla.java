package com.example.backend;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;


@Entity
@Table(name = "charlas")
@Data
public class Charla {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    
    @Column(length = 1000)
    private String descripcion;
    
    private String lugar;
    private LocalDateTime fechaHora;
    private int cuposTotales;
    private int cuposDisponibles;

    @ManyToOne
    private Expositor expositor;
}
