package com.imc.imc_tracker.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicion_imc")
public class MedicionIMC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
    @DecimalMax(value = "500.0", message = "El peso no puede ser mayor a 500 kg")
    @Column(nullable = false)
    private Double peso;

    @NotNull(message = "El IMC es obligatorio")
    @Column(nullable = false)
    private Double imc;

    @NotBlank(message = "La clasificación es obligatoria")
    @Column(nullable = false, length = 20)
    private String clasificacion;

    @Column(name = "fecha_medicion", nullable = false)
    private LocalDateTime fechaMedicion;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Constructor vacío
    public MedicionIMC() {
    }

    // Constructor con parámetros
    public MedicionIMC(Double peso, Double imc, String clasificacion, Usuario usuario) {
        this.peso = peso;
        this.imc = imc;
        this.clasificacion = clasificacion;
        this.usuario = usuario;
    }

    @PrePersist
    protected void onCreate() {
        if (fechaMedicion == null) {
            fechaMedicion = LocalDateTime.now();
        }
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getImc() {
        return imc;
    }

    public void setImc(Double imc) {
        this.imc = imc;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public LocalDateTime getFechaMedicion() {
        return fechaMedicion;
    }

    public void setFechaMedicion(LocalDateTime fechaMedicion) {
        this.fechaMedicion = fechaMedicion;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicionIMC)) return false;
        return id != null && id.equals(((MedicionIMC) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}