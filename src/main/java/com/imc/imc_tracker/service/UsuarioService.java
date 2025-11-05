package com.imc.imc_tracker.service;


import com.imc.imc_tracker.dto.LoginDTO;
import com.imc.imc_tracker.dto.UsuarioRegistroDTO;
import com.imc.imc_tracker.model.Usuario;
import com.imc.imc_tracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en el sistema
     * @param dto Datos del usuario a registrar
     * @return Usuario registrado
     * @throws IllegalArgumentException si el nombre de usuario ya existe
     */
    public Usuario registrarUsuario(UsuarioRegistroDTO dto) {
        // Verificar si el nombre de usuario ya existe
        if (usuarioRepository.existsByNombreUsuario(dto.getNombreUsuario())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword())); // Encriptar contraseña
        usuario.setEdad(dto.getEdad());
        usuario.setSexo(dto.getSexo());
        usuario.setEstatura(dto.getEstatura());

        return usuarioRepository.save(usuario);
    }

    /**
     * Autentica un usuario
     * @param loginDTO Credenciales del usuario
     * @return Usuario si las credenciales son correctas
     * @throws IllegalArgumentException si las credenciales son inválidas
     */
    public Usuario autenticar(LoginDTO loginDTO) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(loginDTO.getNombreUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña incorrectos"));

        // Verificar contraseña
        if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        return usuario;
    }

    /**
     * Busca un usuario por su nombre de usuario
     * @param nombreUsuario Nombre de usuario a buscar
     * @return Usuario encontrado
     * @throws IllegalArgumentException si no se encuentra el usuario
     */
    @Transactional(readOnly = true)
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    /**
     * Busca un usuario por su ID
     * @param id ID del usuario
     * @return Usuario encontrado
     * @throws IllegalArgumentException si no se encuentra el usuario
     */
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    /**
     * Verifica si existe un usuario con el nombre de usuario dado
     * @param nombreUsuario Nombre de usuario a verificar
     * @return true si existe, false en caso contrario
     */
    @Transactional(readOnly = true)
    public boolean existeNombreUsuario(String nombreUsuario) {
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }

    /**
     * Actualiza los datos de un usuario
     * @param id ID del usuario a actualizar
     * @param dto Nuevos datos del usuario
     * @return Usuario actualizado
     */
    public Usuario actualizarUsuario(Long id, UsuarioRegistroDTO dto) {
        Usuario usuario = buscarPorId(id);

        // Verificar si el nuevo nombre de usuario ya existe (si cambió)
        if (!usuario.getNombreUsuario().equals(dto.getNombreUsuario()) &&
                usuarioRepository.existsByNombreUsuario(dto.getNombreUsuario())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setEdad(dto.getEdad());
        usuario.setSexo(dto.getSexo());
        usuario.setEstatura(dto.getEstatura());

        // Solo actualizar contraseña si se proporciona una nueva
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }
}
