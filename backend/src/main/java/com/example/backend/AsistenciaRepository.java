package com.example.backend;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndCharlaId(Long usuarioId, Long charlaId);
}
