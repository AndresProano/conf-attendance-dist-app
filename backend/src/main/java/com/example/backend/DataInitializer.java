package com.example.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CharlaRepository charlaRepository;
    private final ExpositorRepository expositorRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final UsuarioRepository usuarioRepository;

    public DataInitializer(CharlaRepository charlaRepository,
                           ExpositorRepository expositorRepository,
                           AsistenciaRepository asistenciaRepository,
                           UsuarioRepository usuarioRepository) {
        this.charlaRepository = charlaRepository;
        this.expositorRepository = expositorRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Force a clean reseed so the updated agenda/speakers always replace old records.
        asistenciaRepository.deleteAll();
        charlaRepository.deleteAll();
        expositorRepository.deleteAll();
        usuarioRepository.deleteAll();

        LocalDate fechaMar19 = LocalDate.of(2026, 3, 19);
        LocalDate fechaMar20 = LocalDate.of(2026, 3, 20);

        // Expositores

        Expositor danielAvila = createExpositor("Daniel Avila",
                "Gerente Planificacion y Logistica");

        Expositor pauloLopez = createExpositor("Paulo Lopez",
                "Presidente del Directorio SERTECPET Ecuador");

        Expositor vinicioTroncoso = createExpositor("Vinicio Troncoso",
                "Gerente General");

        Expositor ignacioBustamante = createExpositor("Ignacio Bustamante",
                "Presidente Ejecutivo");

        Expositor santiagoCampana = createExpositor("Santiago Campana",
                "Chairman");

        Expositor enithCarrion = createExpositor("Enith Carrion",
                "Master en Gestion de Proyectos y Master en Telematica");

        Expositor jorgeLuisCepeda = createExpositor("Jorge Luis Cepeda",
                "CEO y Cofundador");

        Expositor oswaldoCortez = createExpositor("Oswaldo Cortez",
                "Gerente Regional (LATAM) de Ingenieria y Construccion");

        Expositor patricioCrespo = createExpositor("Patricio Crespo",
                "Director de Operaciones");

        Expositor javierEstrella = createExpositor("Javier Estrella",
                "Gerente General y Fundador");

        Expositor victorHerrera = createExpositor("Victor Herrera",
                "Profesor y Coordinador de Sostenibilidad");

        Expositor jorgeLuisJaramillo = createExpositor("Jorge Luis Jaramillo",
                "Gerente General");

        Expositor marcelaMoreta = createExpositor("Marcela Moreta",
                "Vicepresidente Ejecutivo y Directora Comercial");

        Expositor marcelaReinoso = createExpositor("Marcela Reinoso",
                "Consultora Independiente");

        Expositor dianaRojas = createExpositor("Diana Rojas",
                "Directora de Prospectiva y Planificacion Electrica");

        Expositor eduardoRosero = createExpositor("Eduardo Rosero",
                "Presidente");

        Expositor rafaelSoria = createExpositor("Rafael Soria",
                "Profesor e Investigador");

        Expositor telmoSanchez = createExpositor("Telmo Sanchez",
                "Gerente General");

        Expositor juanCarlosSantamaria = createExpositor("Juan Carlos Santamaria",
                "Gerente de Ingenieria y Negocios");

        Expositor pamelaUbidia = createExpositor("Pamela Ubidia",
                "Gerente de Operaciones");

        Expositor henryYandun = createExpositor("Henry Yandun",
                "Presidente de Kubiec / Presidente de Constructores Positivos");

        // Charlas (19 de marzo - morning)
        createCharla("Registro & Red de Contactos",
                "Teatro Calderón de la Barca, USFQ Campus",
                fechaMar19, LocalTime.of(8, 0), LocalTime.of(8, 30),
                200, null);

        createCharla("Bienvenida, Anuncios & Discurso de Apertura",
                "Teatro Calderón de la Barca, USFQ Campus",
                fechaMar19, LocalTime.of(8, 30), LocalTime.of(9, 0),
                300, null);

        createCharla("Como Emprender con Exito en la Profesion de Ingenieria Mecanica",
                "Teatro Calderon de la Barca, USFQ Campus",
                fechaMar19, LocalTime.of(9, 0), LocalTime.of(10, 0),
                220, henryYandun);

        createCharla("ASME Innovative Additive Manufacturing 3D (IAM3D) Challenge",
                "Teatro Epicuro",
                fechaMar19, LocalTime.of(10, 0), LocalTime.of(11, 0),
                120, null);

        createCharla("ADSTREN: Primero Ingenieria, Despues Empresa",
                "Teatro Calderon de la Barca, USFQ Campus",
                fechaMar19, LocalTime.of(10, 0), LocalTime.of(11, 0),
                220, telmoSanchez);

        createCharla("Intermedio & Refrigerio",
                "USFQ Campus",
                fechaMar19, LocalTime.of(11, 0), LocalTime.of(11, 45),
                500, null);

        createCharla("ASME Innovative Additive Manufacturing 3D (IAM3D) Challenge",
                "Teatro Epicuro",
                fechaMar19, LocalTime.of(11, 45), LocalTime.of(12, 45),
                120, null);

        createCharla("Emprendimiento en el Sector de HVAC",
                "Teatro Calderon de la Barca, USFQ Campus",
                fechaMar19, LocalTime.of(11, 45), LocalTime.of(12, 45),
                220, marcelaMoreta);

        // Charlas (19 de marzo - afternoon/evening)
        createCharla("Receso para Almuerzo",
                "USFQ Campus",
                fechaMar19, LocalTime.of(12, 45), LocalTime.of(14, 30),
                500, null);

        createCharla("ASME Innovative Additive Manufacturing 3D (IAM3D) Challenge",
                "Teatro Epicuro",
                fechaMar19, LocalTime.of(14, 30), LocalTime.of(16, 30),
                120, null);

        createCharla("Emprendimiento en el Sector del Vidrio y el Aluminio",
                "Teatro Calderon de la Barca, USFQ Campus",
                fechaMar19, LocalTime.of(14, 30), LocalTime.of(15, 30),
                220, jorgeLuisJaramillo);

        createCharla("Emprendimiento Joven en la Industria Petrolera",
                "Teatro Calderon de la Barca, USFQ Campus",
                fechaMar19, LocalTime.of(15, 30), LocalTime.of(16, 30),
                220, juanCarlosSantamaria);

        createCharla("Intermedio & Refrigerio",
                "USFQ Campus",
                fechaMar19, LocalTime.of(16, 30), LocalTime.of(17, 0),
                500, null);

        createCharla("FORO: Las Perspectivas y Oportunidades del Sector Energetico en el Ecuador",
                "Teatro Calderon de la Barca, USFQ Campus",
                fechaMar19, LocalTime.of(17, 0), LocalTime.of(18, 30),
                240,
                vinicioTroncoso, victorHerrera, marcelaReinoso,
                dianaRojas, eduardoRosero, rafaelSoria);

        createCharla("ASME Innovative Additive Manufacturing 3D (IAM3D) Challenge",
                "Teatro Epicuro",
                fechaMar19, LocalTime.of(17, 0), LocalTime.of(18, 0),
                120, null);

        createCharla("ASME Innovative Additive Manufacturing 3D (IAM3D) Challenge - Deliberacion de los jueces",
                "Teatro Epicuro",
                fechaMar19, LocalTime.of(18, 0), LocalTime.of(18, 30),
                120, null);

        createCharla("Red de Contactos",
                "USFQ Campus",
                fechaMar19, LocalTime.of(18, 30), LocalTime.of(19, 30),
                500, null);

        // Charlas (20 de marzo - morning)
        createCharla("Registro & Red de Contactos",
                "Teatro Calderon de la Barca, USFQ Campus",
                fechaMar20, LocalTime.of(8, 0), LocalTime.of(8, 30),
                200, null);

        createCharla("Emprender con Proposito: Disciplina, Humildad y el Sueno Llamado SERTECPET",
                "Teatro Calderon de la Barca, USFQ Campus",
                fechaMar20, LocalTime.of(9, 0), LocalTime.of(10, 0),
                220, pauloLopez);

        createCharla("ASME Elevator Pitch Competition, patrocinado por ASME Old Guard Committee",
                "Teatro Epicuro",
                fechaMar20, LocalTime.of(9, 0), LocalTime.of(10, 0),
                120, null);

        createCharla("Emprendimiento en el Sector de Infraestructura",
                "Teatro Calderon de la Barca, USFQ Campus",
                fechaMar20, LocalTime.of(10, 0), LocalTime.of(11, 0),
                220, jorgeLuisCepeda);

        createCharla("ASME Oral Competition, patrocinado por ASME Old Guard Committee",
                "Teatro Epicuro",
                fechaMar20, LocalTime.of(10, 0), LocalTime.of(11, 0),
                120, null);

        createCharla("Intermedio & Refrigerio",
                "USFQ Campus",
                fechaMar20, LocalTime.of(11, 0), LocalTime.of(11, 45),
                500, null);

        createCharla("ASME Oral Competition, patrocinado por ASME Old Guard Committee",
                "Teatro Epicuro",
                fechaMar20, LocalTime.of(11, 45), LocalTime.of(12, 45),
                120, null);

        createCharla("Emprendimiento en el Sector Maderero",
                "Teatro Calderon de la Barca, USFQ Campus",
                fechaMar20, LocalTime.of(11, 45), LocalTime.of(12, 45),
                220, ignacioBustamante);

        // Charlas (20 de marzo - afternoon)
        createCharla("Receso para Almuerzo",
                "USFQ Campus",
                fechaMar20, LocalTime.of(12, 45), LocalTime.of(14, 30),
                500, null);

        createCharla("Emprendimiento en la Industria Metal-Mecanica",
                "Teatro Calderon de la Barca, USFQ Campus",
                fechaMar20, LocalTime.of(14, 30), LocalTime.of(15, 30),
                220, javierEstrella);

        createCharla("ASME Oral Competition, patrocinado por ASME Old Guard Committee",
                "Teatro Epicuro",
                fechaMar20, LocalTime.of(14, 30), LocalTime.of(18, 0),
                120, null);

        createCharla("FORO: Ask Me Anything: Una Platica con la Alta Gerencia",
                "Teatro Calderon de la Barca, USFQ Campus",
                fechaMar20, LocalTime.of(15, 30), LocalTime.of(17, 0),
                240,
                danielAvila, santiagoCampana, enithCarrion,
                oswaldoCortez, patricioCrespo, pamelaUbidia);

        createCharla("Ceremonia de Clausura y Entrega de Premios",
                "Salon Azul",
                fechaMar20, LocalTime.of(17, 0), LocalTime.of(17, 45),
                300, null);

        System.out.println("Base de datos inicializada con datos de la conferencia (30 charlas, 21 expositores).");
    }

    private Expositor createExpositor(String nombre, String bio) {
        Expositor e = new Expositor();
        e.setNombre(nombre);
        e.setBio(bio);
        return expositorRepository.save(e);
    }

    private void createCharla(String titulo, String lugar, LocalDate fecha,
                              LocalTime desde, LocalTime hasta,
                              int cupos, Expositor... expositores) {
        Charla c = new Charla();
        c.setTitulo(titulo);
        c.setLugar(lugar);
        c.setFecha(fecha);
        c.setHoraDesde(desde);
        c.setHoraHasta(hasta);
        c.setCuposTotales(cupos);
        c.setCuposDisponibles(cupos);
        if (expositores != null && expositores.length > 0) {
            c.getExpositores().addAll(Arrays.asList(expositores));
        }
        charlaRepository.save(c);
    }
}
