package com.example.backend;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        charla.setLugar(payload.get("lugar").toString());

        // Supports new payload (fecha + horaDesde + horaHasta) and old payload (fechaHora).
        if (payload.containsKey("fecha") && payload.containsKey("horaDesde") && payload.containsKey("horaHasta")) {
            charla.setFecha(LocalDate.parse(payload.get("fecha").toString()));
            charla.setHoraDesde(LocalTime.parse(payload.get("horaDesde").toString()));
            charla.setHoraHasta(LocalTime.parse(payload.get("horaHasta").toString()));
        } else if (payload.containsKey("fechaHora")) {
            LocalDateTime inicio = LocalDateTime.parse(payload.get("fechaHora").toString());
            charla.setFecha(inicio.toLocalDate());
            charla.setHoraDesde(inicio.toLocalTime());
            charla.setHoraHasta(inicio.toLocalTime().plusHours(1));
        } else {
            return ResponseEntity.badRequest().body("Se requiere fecha y rango horario");
        }

        int cuposTotales = ((Number) payload.get("cuposTotales")).intValue();
        charla.setCuposTotales(cuposTotales);
        charla.setCuposDisponibles(cuposTotales);

        if (payload.containsKey("expositorIds") && payload.get("expositorIds") instanceof List<?> rawIds) {
            List<Long> ids = new ArrayList<>();
            for (Object rawId : rawIds) {
                ids.add(Long.valueOf(rawId.toString()));
            }
            charla.getExpositores().addAll(expositorRepository.findAllById(ids));
        } else if (payload.containsKey("expositorId")) {
            Long expId = Long.valueOf(payload.get("expositorId").toString());
            expositorRepository.findById(expId).ifPresent(exp -> charla.getExpositores().add(exp));
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

        // 4. Registrar asistencia
        Asistencia asistencia = new Asistencia();
        asistencia.setUsuario(usuario);
        asistencia.setCharla(charla);
        asistencia.setFechaRegistro(LocalDateTime.now());
        asistenciaRepository.save(asistencia);

        return ResponseEntity.ok("Check-in exitoso para: " + charla.getTitulo());
    }

    @GetMapping(value = "/charlas/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getCharlaQr(@PathVariable Long id,
                                               @RequestParam(defaultValue = "300") int size) {
        Optional<Charla> charlaOpt = charlaRepository.findById(id);
        if (charlaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(String.valueOf(id), BarcodeFormat.QR_CODE, size, size);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(out.toByteArray());
        } catch (WriterException | IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/charlas/qr-all", produces = "application/zip")
    public ResponseEntity<byte[]> getAllQrCodes(@RequestParam(defaultValue = "400") int size) {
        List<Charla> charlas = charlaRepository.findAll();
        if (charlas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            ByteArrayOutputStream zipOut = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(zipOut);
            QRCodeWriter writer = new QRCodeWriter();

            for (Charla charla : charlas) {
                BitMatrix bitMatrix = writer.encode(
                        String.valueOf(charla.getId()), BarcodeFormat.QR_CODE, size, size);
                ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOut);

                String fileName = sanitizeFileName(charla.getId() + "-" + charla.getTitulo()) + ".png";
                zos.putNextEntry(new ZipEntry(fileName));
                zos.write(pngOut.toByteArray());
                zos.closeEntry();
            }

            zos.close();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"qr-codes-charlas.zip\"")
                    .body(zipOut.toByteArray());
        } catch (WriterException | IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String sanitizeFileName(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return normalized.replaceAll("[^a-zA-Z0-9\\-]", "_").replaceAll("_+", "_");
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
