package com.imc.imc_tracker.dto;


import jakarta.validation.constraints.*;

public class UsuarioRegistroDTO {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 100)
    private String nombreCompleto;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50)
    private String nombreUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 15, message = "La edad debe ser mayor o igual a 15 años")
    @Max(value = 120)
    private Integer edad;

    @NotBlank(message = "El sexo es obligatorio")
    private String sexo;

    @NotNull(message = "La estatura es obligatoria")
    @DecimalMin(value = "1.0", message = "La estatura debe ser mayor o igual a 1.0 metros")
    @DecimalMax(value = "2.5", message = "La estatura debe ser menor o igual a 2.5 metros")
    private Double estatura;

    // Constructores
    public UsuarioRegistroDTO() {}

    // Getters y Setters
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public Double getEstatura() { return estatura; }
    public void setEstatura(Double estatura) { this.estatura = estatura; }
}