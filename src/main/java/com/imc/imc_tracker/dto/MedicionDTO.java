package com.imc.imc_tracker.dto;

import jakarta.validation.constraints.*;

public class MedicionDTO {

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
    @DecimalMax(value = "500.0", message = "El peso no puede ser mayor a 500 kg")
    private Double peso;

    private String notas;

    // Constructores
    public MedicionDTO() {}

    public MedicionDTO(Double peso) {
        this.peso = peso;
    }

    // Getters y Setters
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
