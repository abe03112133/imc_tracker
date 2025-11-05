package com.imc.imc_tracker.dto;

import java.time.LocalDateTime;

public class MedicionResponseDTO {

    private Long id;
    private Double peso;
    private Double imc;
    private String clasificacion;
    private LocalDateTime fechaMedicion;
    private String notas;

    // Constructores
    public MedicionResponseDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getImc() { return imc; }
    public void setImc(Double imc) { this.imc = imc; }

    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }

    public LocalDateTime getFechaMedicion() { return fechaMedicion; }
    public void setFechaMedicion(LocalDateTime fechaMedicion) { this.fechaMedicion = fechaMedicion; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
