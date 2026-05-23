package Modelo;

import java.sql.Date;

public class Pieza {
    private int id;
    private String nombre;
    private int stock;
    private Date fechaMovimiento; 
    private String estado;

    public Pieza() {}

    // Constructor 
    public Pieza(int id, String nombre, int stock, Date fechaMovimiento, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.fechaMovimiento = fechaMovimiento;
        this.estado = estado;

    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public Date getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(Date fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
}