package com.imc.imc_tracker.controller;

import com.imc.imc_tracker.model.Usuario;
import com.imc.imc_tracker.service.IMCService;
import com.imc.imc_tracker.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private IMCService imcService;

    /**
     * Muestra el dashboard principal con calculadora de IMC
     */
    @GetMapping
    public String mostrarDashboard(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        // Verificar que el usuario esté logueado
        if (usuarioId == null) {
            return "redirect:/login";
        }

        try {
            // Obtener datos del usuario
            Usuario usuario = usuarioService.buscarPorId(usuarioId);

            // Obtener estadísticas
            Long totalMediciones = imcService.contarMediciones(usuarioId);

            // Agregar datos al modelo
            model.addAttribute("usuario", usuario);
            model.addAttribute("totalMediciones", totalMediciones);

            return "dashboard";

        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar dashboard: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Muestra la página de historial completo
     */
    @GetMapping("/historial")
    public String mostrarHistorial(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        try {
            Usuario usuario = usuarioService.buscarPorId(usuarioId);
            model.addAttribute("usuario", usuario);

            return "historial";

        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar historial: " + e.getMessage());
            return "error";
        }
    }
}
