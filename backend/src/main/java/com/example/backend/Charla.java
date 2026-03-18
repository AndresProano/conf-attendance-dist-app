package com.example.backend;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "charlas")
@Data
public class Charla {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String lugar;
    private LocalDate fecha;
    private LocalTime horaDesde;
    private LocalTime horaHasta;
    private int cuposTotales;
    private int cuposDisponibles;

    @ManyToMany
    @JoinTable(
            name = "charla_expositores",
            joinColumns = @JoinColumn(name = "charla_id"),
            inverseJoinColumns = @JoinColumn(name = "expositor_id")
    )
    private List<Expositor> expositores = new ArrayList<>();
}
