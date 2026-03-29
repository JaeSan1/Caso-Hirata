package Modelo;

public class Camion {
    private int id;
    private String marca;
    private String modelo;
    private int anio;
    private double kmActual; 
    private double kmUltimoMantenimiento; 
    private Integer conductorId;
    
    public Camion() {
    }

    public Camion(int id, String marca, String modelo, int anio, double kmActual, double kmUltimoMantenimiento, Integer conductorId) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.kmActual = kmActual;
        this.kmUltimoMantenimiento = kmUltimoMantenimiento;
        this.conductorId = conductorId;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; } 
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public double getKmActual() { return kmActual; }
    public void setKmActual(double kmActual) { this.kmActual = kmActual; }

    public double getKmUltimoMantenimiento() { return kmUltimoMantenimiento; }
    public void setKmUltimoMantenimiento(double kmUltimoMantenimiento) { this.kmUltimoMantenimiento = kmUltimoMantenimiento; }

    public Integer getConductorId() { return conductorId; }
    public void setConductorId(Integer conductorId) { this.conductorId = conductorId; }

    public boolean requiereMantenimiento() {
        return (this.kmActual - this.kmUltimoMantenimiento) >= 5000;
    }
}