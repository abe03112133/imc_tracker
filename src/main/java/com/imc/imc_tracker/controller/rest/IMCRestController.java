package com.imc.imc_tracker.controller.rest;

import com.imc.imc_tracker.dto.IMCResultadoDTO;
import com.imc.imc_tracker.dto.MedicionDTO;
import com.imc.imc_tracker.dto.MedicionResponseDTO;
import com.imc.imc_tracker.service.IMCService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller para las operaciones de IMC
 * Los datos del histórico se consumen a través de estos endpoints REST
 */
@RestController
@RequestMapping("/api/imc")
public class IMCRestController {

    @Autowired
    private IMCService imcService;

    /**
     * Endpoint REST para calcular y registrar una nueva medición de IMC
     * POST /api/imc/calcular
     * @param medicionDTO Datos de la medición
     * @param session Sesión HTTP para obtener el usuario logueado
     * @return Resultado del cálculo
     */
    @PostMapping("/calcular")
    public ResponseEntity<?> calcularIMC(@Valid @RequestBody MedicionDTO medicionDTO,
                                         HttpSession session) {
        try {
            // Verificar que el usuario esté logueado
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(crearRespuestaError("Debe iniciar sesión para calcular su IMC"));
            }

            // Calcular y registrar medición
            IMCResultadoDTO resultado = imcService.registrarMedicion(usuarioId, medicionDTO);

            return ResponseEntity.ok(resultado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al calcular IMC: " + e.getMessage()));
        }
    }

    /**
     * Endpoint REST para obtener el historial completo de mediciones
     * GET /api/imc/historial
     * @param session Sesión HTTP
     * @return Lista de mediciones del usuario
     */
    @GetMapping("/historial")
    public ResponseEntity<?> obtenerHistorial(HttpSession session) {
        try {
            // Verificar que el usuario esté logueado
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(crearRespuestaError("Debe iniciar sesión"));
            }

            List<MedicionResponseDTO> historial = imcService.obtenerHistorial(usuarioId);

            return ResponseEntity.ok(historial);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al obtener historial: " + e.getMessage()));
        }
    }

    /**
     * Endpoint REST para obtener las últimas mediciones
     * GET /api/imc/ultimas
     * @param session Sesión HTTP
     * @return Lista de las últimas 10 mediciones
     */
    @GetMapping("/ultimas")
    public ResponseEntity<?> obtenerUltimasMediciones(HttpSession session) {
        try {
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(crearRespuestaError("Debe iniciar sesión"));
            }

            List<MedicionResponseDTO> ultimas = imcService.obtenerUltimasMediciones(usuarioId);

            return ResponseEntity.ok(ultimas);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al obtener mediciones: " + e.getMessage()));
        }
    }

    /**
     * Endpoint REST para obtener la última medición
     * GET /api/imc/ultima
     * @param session Sesión HTTP
     * @return Última medición del usuario
     */
    @GetMapping("/ultima")
    public ResponseEntity<?> obtenerUltimaMedicion(HttpSession session) {
        try {
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(crearRespuestaError("Debe iniciar sesión"));
            }

            MedicionResponseDTO ultima = imcService.obtenerUltimaMedicion(usuarioId);

            if (ultima == null) {
                return ResponseEntity.ok(crearRespuestaInfo("No hay mediciones registradas"));
            }

            return ResponseEntity.ok(ultima);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al obtener última medición: " + e.getMessage()));
        }
    }

    /**
     * Endpoint REST para obtener estadísticas del usuario
     * GET /api/imc/estadisticas
     * @param session Sesión HTTP
     * @return Estadísticas del usuario
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas(HttpSession session) {
        try {
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(crearRespuestaError("Debe iniciar sesión"));
            }

            Long totalMediciones = imcService.contarMediciones(usuarioId);
            MedicionResponseDTO ultimaMedicion = imcService.obtenerUltimaMedicion(usuarioId);

            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("totalMediciones", totalMediciones);
            estadisticas.put("ultimaMedicion", ultimaMedicion);

            return ResponseEntity.ok(estadisticas);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al obtener estadísticas: " + e.getMessage()));
        }
    }

    /**
     * Endpoint REST para eliminar una medición
     * DELETE /api/imc/{id}
     * @param id ID de la medición
     * @param session Sesión HTTP
     * @return Respuesta de éxito o error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMedicion(@PathVariable Long id, HttpSession session) {
        try {
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(crearRespuestaError("Debe iniciar sesión"));
            }

            imcService.eliminarMedicion(id);

            return ResponseEntity.ok(crearRespuestaExito("Medición eliminada correctamente"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al eliminar medición: " + e.getMessage()));
        }
    }

    // Métodos auxiliares para crear respuestas JSON

    private Map<String, String> crearRespuestaError(String mensaje) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", mensaje);
        respuesta.put("status", "error");
        return respuesta;
    }

    private Map<String, String> crearRespuestaExito(String mensaje) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", mensaje);
        respuesta.put("status", "success");
        return respuesta;
    }

    private Map<String, String> crearRespuestaInfo(String mensaje) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", mensaje);
        respuesta.put("status", "info");
        return respuesta;
    }
}