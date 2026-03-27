package Modelo;




public class Camion {
    private int id;
    private String marca;
    private String modelo;
    private int anio;
    private double km_actual;
    private double km_ultimo_mantenimiento;
    private Integer conductorId;
    
    public Camion(){

    }

    // Constructor
    public Camion(int id, String marca, String modelo, int anio, double km_actual, double km_ultimo_mantenimiento, Integer conductorId) {
        this.id = id;
        this.marca = marca;
        this.modelo =modelo;
        this.anio = anio;
        this.km_actual =km_actual;
        this.km_ultimo_mantenimiento = km_ultimo_mantenimiento;
        this.conductorId = conductorId;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmarca() {
        return marca;
    }

    public void setmarca(String marca) {
        this.marca = marca;
    }

    public String getConductor() {
        return modelo;
    }

    public void setmodelo(String modelo) {
        this.modelo = modelo;
    }

    public int getanio() {
        return anio;
    }

    public void setanio(int anio) {
        this.anio = anio;
    }

    public double iskm_actual() {
        return km_actual;
    }

    public void setkm_actual(double km_actual) {
        this.km_actual = km_actual;
    }

    public double getkm_ultimo_mantenimiento() {
        return km_ultimo_mantenimiento;
    }

    public void setkm_ultimo_mantenimiento(double km_ultimo_mantenimiento) {
        this.km_ultimo_mantenimiento = km_ultimo_mantenimiento;
    }
    public Integer getconductorId() {
        return conductorId;
    
    }
    public void setconductorId(Integer conductorId){
        this.conductorId = conductorId;
    }
}