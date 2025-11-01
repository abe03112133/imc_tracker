package com.imc.imc_tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Column(name = "nombre_usuario", unique = true, nullable = false, length = 50)
    private String nombreUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 15, message = "La edad debe ser mayor o igual a 15 años")
    @Max(value = 120, message = "La edad no puede ser mayor a 120 años")
    @Column(nullable = false)
    private Integer edad;

    @NotBlank(message = "El sexo es obligatorio")
    @Pattern(regexp = "^(Masculino|Femenino|Otro)$", message = "El sexo debe ser: Masculino, Femenino u Otro")
    @Column(nullable = false, length = 10)
    private String sexo;

    @NotNull(message = "La estatura es obligatoria")
    @DecimalMin(value = "1.0", message = "La estatura debe ser mayor o igual a 1.0 metros")
    @DecimalMax(value = "2.5", message = "La estatura debe ser menor o igual a 2.5 metros")
    @Column(nullable = false)
    private Double estatura;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicionIMC> mediciones = new ArrayList<>();

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(String nombreCompleto, String nombreUsuario, String password,
                   Integer edad, String sexo, Double estatura) {
        this.nombreCompleto = nombreCompleto;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.edad = edad;
        this.sexo = sexo;
        this.estatura = estatura;
    }

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        ultimaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Double getEstatura() {
        return estatura;
    }

    public void setEstatura(Double estatura) {
        this.estatura = estatura;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public List<MedicionIMC> getMediciones() {
        return mediciones;
    }

    public void setMediciones(List<MedicionIMC> mediciones) {
        this.mediciones = mediciones;
    }

    // Método helper para agregar mediciones
    public void addMedicion(MedicionIMC medicion) {
        mediciones.add(medicion);
        medicion.setUsuario(this);
    }

    // Método helper para remover mediciones
    public void removeMedicion(MedicionIMC medicion) {
        mediciones.remove(medicion);
        medicion.setUsuario(null);
    }
}