package com.imc.imc_tracker.controller;

import com.imc.imc_tracker.dto.LoginDTO;
import com.imc.imc_tracker.dto.UsuarioRegistroDTO;
import com.imc.imc_tracker.model.Usuario;
import com.imc.imc_tracker.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Página de inicio - redirige al login
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    /**
     * Muestra el formulario de registro
     */
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioRegistroDTO());
        return "registro";
    }

    /**
     * Procesa el formulario de registro
     */
    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuarioDTO") UsuarioRegistroDTO usuarioDTO,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        // Si hay errores de validación, volver al formulario
        if (result.hasErrors()) {
            return "registro";
        }

        try {
            // Registrar usuario
            usuarioService.registrarUsuario(usuarioDTO);

            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado exitosamente. Por favor inicia sesión.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "registro";
        }
    }

    /**
     * Muestra el formulario de login
     */
    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model, HttpSession session) {
        // Si ya está logueado, redirigir al dashboard
        if (session.getAttribute("usuarioId") != null) {
            return "redirect:/dashboard";
        }

        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    /**
     * Procesa el login
     */
    @PostMapping("/login")
    public String procesarLogin(@Valid @ModelAttribute("loginDTO") LoginDTO loginDTO,
                                BindingResult result,
                                HttpSession session,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        if (result.hasErrors()) {
            return "login";
        }

        try {
            // Autenticar usuario
            Usuario usuario = usuarioService.autenticar(loginDTO);

            // Guardar en sesión
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("nombreUsuario", usuario.getNombreUsuario());
            session.setAttribute("nombreCompleto", usuario.getNombreCompleto());

            redirectAttributes.addFlashAttribute("mensaje", "Bienvenido, " + usuario.getNombreCompleto());
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

            return "redirect:/dashboard";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Error al iniciar sesión: " + e.getMessage());
            return "login";
        }
    }

    /**
     * Cierra la sesión del usuario
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada correctamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "info");
        return "redirect:/login";
    }

    /**
     * Muestra el perfil del usuario
     */
    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        try {
            Usuario usuario = usuarioService.buscarPorId(usuarioId);
            model.addAttribute("usuario", usuario);
            return "perfil";

        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar perfil: " + e.getMessage());
            return "error";
        }
    }
}
