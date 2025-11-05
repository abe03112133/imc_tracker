package com.imc.imc_tracker.dto;

import java.time.LocalDateTime;

public class IMCResultadoDTO {

    private Double imc;
    private String clasificacion;
    private String mensaje;
    private Double peso;
    private LocalDateTime fecha;

    // Constructores
    public IMCResultadoDTO() {}

    public IMCResultadoDTO(Double imc, String clasificacion, String mensaje, Double peso, LocalDateTime fecha) {
        this.imc = imc;
        this.clasificacion = clasificacion;
        this.mensaje = mensaje;
        this.peso = peso;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Double getImc() { return imc; }
    public void setImc(Double imc) { this.imc = imc; }

    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
