package com.imc.imc_tracker.service;

import com.imc.imc_tracker.dto.IMCResultadoDTO;
import com.imc.imc_tracker.dto.MedicionDTO;
import com.imc.imc_tracker.dto.MedicionResponseDTO;
import com.imc.imc_tracker.model.MedicionIMC;
import com.imc.imc_tracker.model.Usuario;
import com.imc.imc_tracker.repository.MedicionIMCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IMCService {

    @Autowired
    private MedicionIMCRepository medicionRepository;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Calcula el IMC dado el peso y la estatura
     * Fórmula: IMC = peso (kg) / [estatura (m)]²
     * @param peso Peso en kilogramos
     * @param estatura Estatura en metros
     * @return Valor del IMC redondeado a 2 decimales
     */
    public Double calcularIMC(Double peso, Double estatura) {
        if (peso <= 0 || estatura <= 0) {
            throw new IllegalArgumentException("El peso y la estatura deben ser mayores a 0");
        }

        double imc = peso / (estatura * estatura);
        return Math.round(imc * 100.0) / 100.0; // Redondear a 2 decimales
    }

    /**
     * Clasifica el IMC según los rangos de la OMS
     * @param imc Valor del IMC
     * @return Clasificación (Bajo peso, Normal, Sobrepeso, Obesidad)
     */
    public String clasificarIMC(Double imc) {
        if (imc < 18.5) {
            return "Bajo peso";
        } else if (imc >= 18.5 && imc < 25) {
            return "Normal";
        } else if (imc >= 25 && imc < 30) {
            return "Sobrepeso";
        } else {
            return "Obesidad";
        }
    }

    /**
     * Obtiene un mensaje informativo según la clasificación del IMC
     * @param clasificacion Clasificación del IMC
     * @return Mensaje informativo
     */
    public String obtenerMensajeIMC(String clasificacion) {
        switch (clasificacion) {
            case "Bajo peso":
                return "Tu IMC indica bajo peso. Te recomendamos consultar con un profesional de la salud.";
            case "Normal":
                return "¡Excelente! Tu IMC está en el rango saludable. Mantén un estilo de vida activo.";
            case "Sobrepeso":
                return "Tu IMC indica sobrepeso. Considera mejorar tus hábitos alimenticios y aumentar actividad física.";
            case "Obesidad":
                return "Tu IMC indica obesidad. Te recomendamos consultar con un profesional de la salud.";
            default:
                return "Clasificación desconocida.";
        }
    }

    /**
     * Registra una nueva medición de IMC para un usuario
     * @param usuarioId ID del usuario
     * @param medicionDTO Datos de la medición
     * @return Resultado del cálculo de IMC
     */
    public IMCResultadoDTO registrarMedicion(Long usuarioId, MedicionDTO medicionDTO) {
        // Validar peso
        if (medicionDTO.getPeso() == null || medicionDTO.getPeso() <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor a 0");
        }

        // Obtener usuario
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        // Calcular IMC
        Double imc = calcularIMC(medicionDTO.getPeso(), usuario.getEstatura());
        String clasificacion = clasificarIMC(imc);
        String mensaje = obtenerMensajeIMC(clasificacion);

        // Crear y guardar medición
        MedicionIMC medicion = new MedicionIMC();
        medicion.setPeso(medicionDTO.getPeso());
        medicion.setImc(imc);
        medicion.setClasificacion(clasificacion);
        medicion.setNotas(medicionDTO.getNotas());
        medicion.setUsuario(usuario);

        medicionRepository.save(medicion);

        // Retornar resultado
        return new IMCResultadoDTO(imc, clasificacion, mensaje, medicionDTO.getPeso(), LocalDateTime.now());
    }

    /**
     * Obtiene el historial completo de mediciones de un usuario
     * @param usuarioId ID del usuario
     * @return Lista de mediciones ordenadas por fecha descendente
     */
    @Transactional(readOnly = true)
    public List<MedicionResponseDTO> obtenerHistorial(Long usuarioId) {
        List<MedicionIMC> mediciones = medicionRepository.findByUsuarioIdOrderByFechaMedicionDesc(usuarioId);

        return mediciones.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la última medición de un usuario
     * @param usuarioId ID del usuario
     * @return Última medición o null si no hay mediciones
     */
    @Transactional(readOnly = true)
    public MedicionResponseDTO obtenerUltimaMedicion(Long usuarioId) {
        List<MedicionIMC> mediciones = medicionRepository.findTop10ByUsuarioIdOrderByFechaMedicionDesc(usuarioId);

        if (mediciones.isEmpty()) {
            return null;
        }

        return convertirAResponseDTO(mediciones.get(0));
    }

    /**
     * Obtiene las últimas 10 mediciones de un usuario
     * @param usuarioId ID del usuario
     * @return Lista de las últimas 10 mediciones
     */
    @Transactional(readOnly = true)
    public List<MedicionResponseDTO> obtenerUltimasMediciones(Long usuarioId) {
        List<MedicionIMC> mediciones = medicionRepository.findTop10ByUsuarioIdOrderByFechaMedicionDesc(usuarioId);

        return mediciones.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cuenta el total de mediciones de un usuario
     * @param usuarioId ID del usuario
     * @return Número total de mediciones
     */
    @Transactional(readOnly = true)
    public Long contarMediciones(Long usuarioId) {
        return medicionRepository.countByUsuarioId(usuarioId);
    }

    /**
     * Elimina una medición
     * @param id ID de la medición a eliminar
     */
    public void eliminarMedicion(Long id) {
        medicionRepository.deleteById(id);
    }

    /**
     * Convierte una entidad MedicionIMC a DTO de respuesta
     * @param medicion Entidad a convertir
     * @return DTO de respuesta
     */
    private MedicionResponseDTO convertirAResponseDTO(MedicionIMC medicion) {
        MedicionResponseDTO dto = new MedicionResponseDTO();
        dto.setId(medicion.getId());
        dto.setPeso(medicion.getPeso());
        dto.setImc(medicion.getImc());
        dto.setClasificacion(medicion.getClasificacion());
        dto.setFechaMedicion(medicion.getFechaMedicion());
        dto.setNotas(medicion.getNotas());
        return dto;
    }
}
