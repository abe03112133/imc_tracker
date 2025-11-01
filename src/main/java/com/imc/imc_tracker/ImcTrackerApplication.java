package com.imc.imc_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase principal de la aplicación IMC Tracker
 *
 * @author Abel Mendez
 * @version 1.0
 * @since 2025
 */
@SpringBootApplication
public class ImcTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImcTrackerApplication.class, args);
        System.out.println("\n" +
                "╔═══════════════════════════════════════════════════════════╗\n" +
                "║                                                           ║\n" +
                "║              IMC TRACKER - APLICACIÓN INICIADA            ║\n" +
                "║                                                           ║\n" +
                "║      Accede a la aplicación en:                           ║\n" +
                "║     http://localhost:8089                                 ║\n" +
                "║                                                           ║\n" +
                "║      API REST disponible en:                              ║\n" +
                "║     http://localhost:8089/api/imc                         ║\n" +
                "║                                                           ║\n" +
                "║     Documentación:                                        ║\n" +
                "║     /registro - Registrar nuevo usuario                   ║\n" +
                "║     /login - Iniciar sesión                               ║\n" +
                "║     /dashboard - Calculadora de IMC                       ║\n" +
                "║     /dashboard/historial - Ver historial                  ║\n" +
                "║                                                           ║\n" +
                "║     Usuarios de prueba:                                   ║\n" +
                "║     Usuario: juanperez | Contraseña: password123          ║\n" +
                "║     Usuario: marialopez | Contraseña: password123         ║\n" +
                "║                                                           ║\n" +
                "╚═══════════════════════════════════════════════════════════╝\n");
    }

    /**
     * Configuración CORS para permitir peticiones desde el frontend
     * si se implementa un frontend separado.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:8080", "http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}