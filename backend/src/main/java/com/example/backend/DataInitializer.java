package com.example.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CharlaRepository charlaRepository;
    private final ExpositorRepository expositorRepository;

    public DataInitializer(CharlaRepository charlaRepository, ExpositorRepository expositorRepository) {
        this.charlaRepository = charlaRepository;
        this.expositorRepository = expositorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (expositorRepository.count() == 0) {
            Expositor e1 = new Expositor();
            e1.setNombre("Dra. Elena Ruiz");
            e1.setBio("Experta en sistemas distribuidos y computación en la nube con más de 15 años de experiencia.");
            e1.setFotoUrl("https://i.pravatar.cc/150?u=elena");
            e1.setTwitter("https://twitter.com/elenaruiz");
            e1.setLinkedin("https://linkedin.com/in/elenaruiz");
            e1.setGithub("https://github.com/elenaruiz");
            expositorRepository.save(e1);

            Expositor e2 = new Expositor();
            e2.setNombre("Marco Polo");
            e2.setBio("Ingeniero de Software Senior especializado en frameworks frontend modernos y UX.");
            e2.setFotoUrl("https://i.pravatar.cc/150?u=marco");
            e2.setTwitter("https://twitter.com/marcopolo");
            e2.setLinkedin("https://linkedin.com/in/marcopolo");
            e2.setGithub("https://github.com/marcopolo");
            expositorRepository.save(e2);

            Charla c1 = new Charla();
            c1.setTitulo("Arquitecturas Distribuidas");
            c1.setDescripcion("Una inmersión profunda en sistemas distribuidos modernos y su escalabilidad.");
            c1.setLugar("Auditorio A");
            c1.setFechaHora(LocalDateTime.of(2026, 3, 10, 9, 0));
            c1.setCuposTotales(50);
            c1.setCuposDisponibles(50);
            c1.setExpositor(e1);
            charlaRepository.save(c1);

            Charla c2 = new Charla();
            c2.setTitulo("Vue.js en el Mundo Real");
            c2.setDescripcion("Patrones avanzados y mejores prácticas para aplicaciones a gran escala.");
            c2.setLugar("Sala 204");
            c2.setFechaHora(LocalDateTime.of(2026, 3, 10, 11, 30));
            c2.setCuposTotales(30);
            c2.setCuposDisponibles(30);
            c2.setExpositor(e2);
            charlaRepository.save(c2);
            
            System.out.println("Base de datos inicializada con datos de prueba.");
        }
    }
}
