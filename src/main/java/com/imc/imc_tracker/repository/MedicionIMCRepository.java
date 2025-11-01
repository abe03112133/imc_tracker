package com.imc.imc_tracker.repository;


import com.imc.imc_tracker.model.MedicionIMC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicionIMCRepository extends JpaRepository<MedicionIMC, Long> {

    /**
     * Obtiene todas las mediciones de un usuario ordenadas por fecha descendente
     */
    List<MedicionIMC> findByUsuarioIdOrderByFechaMedicionDesc(Long usuarioId);

    /**
     * Obtiene todas las mediciones de un usuario
     */
    List<MedicionIMC> findByUsuarioId(Long usuarioId);

    /**
     * Obtiene las últimas N mediciones de un usuario
     */
    List<MedicionIMC> findTop10ByUsuarioIdOrderByFechaMedicionDesc(Long usuarioId);

    /**
     * Cuenta el total de mediciones de un usuario
     */
    Long countByUsuarioId(Long usuarioId);
}