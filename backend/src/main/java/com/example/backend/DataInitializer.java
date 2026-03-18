package com.example.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CharlaRepository charlaRepository;
    private final ExpositorRepository expositorRepository;

    public DataInitializer(CharlaRepository charlaRepository,
                           ExpositorRepository expositorRepository) {
        this.charlaRepository = charlaRepository;
        this.expositorRepository = expositorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (charlaRepository.count() > 0) {
            System.out.println("Base de datos ya contiene datos, omitiendo inicialización.");
            return;
        }

        LocalDate fecha = LocalDate.of(2026, 3, 20);

        // ── Expositores ──

        Expositor pauloLopez = createExpositor("Paulo López",
                "Presidente del Directorio SERTECPET Ecuador");

        Expositor jorgeCepeda = createExpositor("Jorge Luis Cepeda",
                "CEO y Cofundador, COINAV S.A.");

        Expositor ignacioBustamante = createExpositor("Ignacio Bustamante",
                "Presidente Ejecutivo, NOVOPAN");

        Expositor javierEstrella = createExpositor("Javier Estrella",
                "Gerente General y Fundador, Induacero");

        // Panelistas del FORO AMA
        createExpositor("Daniel Avila",
                "Gerente Planificación y Logística, Industrial Danec S.A.");
        createExpositor("Santiago Campaña",
                "Chairman, E-Fest Steering Committee de ASME");
        createExpositor("Enith Carrión",
                "Master en Gestión de Proyectos y Master en Telemática, PROINVEST ENERGY");
        createExpositor("Oswaldo Cortez",
                "Gerente Regional (LATAM) de Ingeniería y Construcción, SERTECPET");
        createExpositor("Patricio Crespo",
                "Director de Operaciones, Imptek Saint-Gobain");
        createExpositor("Pamela Ubidia",
                "Gerente de Operaciones, Copiadora Ecuatoriana Ecuacopia S.A.S");

        // ── Charlas (Morning) ──

        // 1
        createCharla("Registro & Red de Contactos",
                "Teatro Calderón de la Barca, USFQ Campus",
                fecha, LocalTime.of(8, 0), LocalTime.of(8, 30),
                200, null);

        // 2
        createCharla("Emprender con Propósito: Disciplina, Humildad y el Sueño Llamado SERTECPET",
                "Teatro Calderón de la Barca, USFQ Campus",
                fecha, LocalTime.of(9, 0), LocalTime.of(10, 0),
                200, pauloLopez);

        // 3
        createCharla("ASME Elevator Pitch Competition, patrocinado por ASME Old Guard Committee",
                "Teatro Epicuro",
                fecha, LocalTime.of(9, 0), LocalTime.of(10, 0),
                100, null);

        // 4
        createCharla("Emprendimiento en el Sector de Infraestructura",
                "Teatro Calderón de la Barca, USFQ Campus",
                fecha, LocalTime.of(10, 0), LocalTime.of(11, 0),
                200, jorgeCepeda);

        // 5
        createCharla("ASME Oral Competition, patrocinado por ASME Old Guard Committee (Sesión 1)",
                "Teatro Epicuro",
                fecha, LocalTime.of(10, 0), LocalTime.of(11, 0),
                100, null);

        // 6
        createCharla("Intermedio & Refrigerio",
                "USFQ Campus",
                fecha, LocalTime.of(11, 0), LocalTime.of(11, 45),
                500, null);

        // 7
        createCharla("ASME Oral Competition, patrocinado por ASME Old Guard Committee (Sesión 2)",
                "Teatro Epicuro",
                fecha, LocalTime.of(11, 45), LocalTime.of(12, 45),
                100, null);

        // 8
        createCharla("Emprendimiento en el Sector Maderero",
                "Teatro Calderón de la Barca, USFQ Campus",
                fecha, LocalTime.of(11, 45), LocalTime.of(12, 45),
                200, ignacioBustamante);

        // ── Charlas (Afternoon) ──

        // 9
        createCharla("Receso para Almuerzo",
                "USFQ Campus",
                fecha, LocalTime.of(12, 45), LocalTime.of(14, 30),
                500, null);

        // 10
        createCharla("Emprendimiento en la Industria Metal-Mecánica",
                "Teatro Calderón de la Barca, USFQ Campus",
                fecha, LocalTime.of(14, 30), LocalTime.of(15, 30),
                200, javierEstrella);

        // 11
        createCharla("ASME Oral Competition, patrocinado por ASME Old Guard Committee (Sesión 3)",
                "Teatro Epicuro",
                fecha, LocalTime.of(14, 30), LocalTime.of(18, 0),
                100, null);

        // 12
        createCharla("FORO: Ask Me Anything - Una Plática con la Alta Gerencia",
                "Teatro Calderón de la Barca, USFQ Campus",
                fecha, LocalTime.of(15, 30), LocalTime.of(17, 0),
                200, null);

        // 13
        createCharla("Ceremonia de Clausura y Entrega de Premios",
                "Salón Azul",
                fecha, LocalTime.of(17, 0), LocalTime.of(17, 45),
                300, null);

        System.out.println("Base de datos inicializada con datos de la conferencia (13 charlas, 10 expositores).");
    }

    private Expositor createExpositor(String nombre, String bio) {
        Expositor e = new Expositor();
        e.setNombre(nombre);
        e.setBio(bio);
        return expositorRepository.save(e);
    }

    private void createCharla(String titulo, String lugar, LocalDate fecha,
                              LocalTime desde, LocalTime hasta,
                              int cupos, Expositor expositor) {
        Charla c = new Charla();
        c.setTitulo(titulo);
        c.setLugar(lugar);
        c.setFecha(fecha);
        c.setHoraDesde(desde);
        c.setHoraHasta(hasta);
        c.setCuposTotales(cupos);
        c.setCuposDisponibles(cupos);
        c.setExpositor(expositor);
        charlaRepository.save(c);
    }
}
