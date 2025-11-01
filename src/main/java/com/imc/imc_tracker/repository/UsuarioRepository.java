package com.imc.imc_tracker.repository;

import com.imc.imc_tracker.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su nombre de usuario
     */
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    /**
     * Verifica si existe un usuario con el nombre de usuario dado
     */
    boolean existsByNombreUsuario(String nombreUsuario);
}