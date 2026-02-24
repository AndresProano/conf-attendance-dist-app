package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ConferenceController {

    @Autowired private CharlaRepository charlaRepository;
    @Autowired private ExpositorRepository expositorRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private AsistenciaRepository asistenciaRepository;

    @GetMapping("/charlas")
    public List<Charla> getCharlas() {
        return charlaRepository.findAll();
    }

    @GetMapping("/expositores")
    public List<Expositor> getExpositores() {
        return expositorRepository.findAll();
    }

    @PostMapping("/expositores")
    public Expositor createExpositor(@RequestBody Expositor expositor) {
        return expositorRepository.save(expositor);
    }

    @PostMapping("/charlas")
    public ResponseEntity<?> createCharla(@RequestBody Map<String, Object> payload) {
        Charla charla = new Charla();
        charla.setTitulo(payload.get("titulo").toString());
        charla.setDescripcion(payload.get("descripcion").toString());
        charla.setLugar(payload.get("lugar").toString());
        charla.setFechaHora(LocalDateTime.parse(payload.get("fechaHora").toString()));
        charla.setCuposTotales((Integer) payload.get("cuposTotales"));
        charla.setCuposDisponibles((Integer) payload.get("cuposTotales"));

        if (payload.containsKey("expositorId")) {
            Long expId = Long.valueOf(payload.get("expositorId").toString());
            expositorRepository.findById(expId).ifPresent(charla::setExpositor);
        }

        return ResponseEntity.ok(charlaRepository.save(charla));
    }

    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn(@RequestBody Map<String, Object> payload) {
        Long charlaId = Long.valueOf(payload.get("charlaId").toString());
        String email = payload.get("email").toString();
        String nombre = payload.get("nombre").toString();
        String banner = payload.get("banner").toString();

        // 1. Obtener o crear usuario
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseGet(() -> {
                    Usuario nuevo = new Usuario();
                    nuevo.setEmail(email);
                    nuevo.setNombre(nombre);
                    nuevo.setBanner(banner);
                    return usuarioRepository.save(nuevo);
                });

        // 2. Verificar charla
        Optional<Charla> charlaOpt = charlaRepository.findById(charlaId);
        if (charlaOpt.isEmpty()) return ResponseEntity.badRequest().body("Charla no encontrada");
        Charla charla = charlaOpt.get();

        // 3. Verificar si ya asistió
        if (asistenciaRepository.existsByUsuarioIdAndCharlaId(usuario.getId(), charla.getId())) {
            return ResponseEntity.ok("Ya estás registrado en esta charla");
        }

        // 4. Verificar cupos
        if (charla.getCuposDisponibles() <= 0) {
            return ResponseEntity.badRequest().body("No hay cupos disponibles");
        }

        // 5. Registrar asistencia y actualizar cupos
        Asistencia asistencia = new Asistencia();
        asistencia.setUsuario(usuario);
        asistencia.setCharla(charla);
        asistencia.setFechaRegistro(LocalDateTime.now());
        asistenciaRepository.save(asistencia);

        charla.setCuposDisponibles(charla.getCuposDisponibles() - 1);
        charlaRepository.save(charla);

        return ResponseEntity.ok("Check-in exitoso para: " + charla.getTitulo());
    }

    @GetMapping("/usuarios/{email}/perfil")
    public ResponseEntity<?> getPerfil(@PathVariable String email) {
        return usuarioRepository.findByEmail(email)
                .map(u -> {
                    List<Asistencia> asistencias = asistenciaRepository.findByUsuarioId(u.getId());
                    return ResponseEntity.ok(Map.of(
                        "nombre", u.getNombre(),
                        "email", u.getEmail(),
                        "banner", u.getBanner() != null ? u.getBanner() : "",
                        "totalCharlas", asistencias.size(),
                        "charlas", asistencias.stream()
                                    .map(a -> a.getCharla().getTitulo())
                                    .collect(Collectors.toList())
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
