package Modelo;

import java.time.LocalDate;

public class Mantenimiento {

    private int id;
    private int camionId;
    private LocalDate fecha;
    private String tipo;
    private String descripcion;
    private Double kmAlMomento; 

    // Constructor vacío
    public Mantenimiento() {
    }

    // Constructor para el Test
    public Mantenimiento(String tipo, Double kmAlMomento) {
        this.tipo = tipo;
        this.kmAlMomento = kmAlMomento;
    } 

    // Constructor para BD
    public Mantenimiento(int id, int camionId, LocalDate fecha, String tipo, String descripcion, Double kmAlMomento) {
        this.id = id;
        this.camionId = camionId;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.kmAlMomento = kmAlMomento;
    }

    // GETTERS Y SETTERS 
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCamionId() { return camionId; }
    public void setCamionId(int camionId) { this.camionId = camionId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getKmAlMomento() { return kmAlMomento; }
    public void setKmAlMomento(Double kmAlMomento) { this.kmAlMomento = kmAlMomento; }
}