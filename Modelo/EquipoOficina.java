package Modelo;

public class EquipoOficina {
    private int id;
    private String nombre;
    private String tipo;
    private String estado;

    public EquipoOficina() {}

    // Constructor con parámetros (Necesario para EquipoOficinaDao)
    public EquipoOficina(int id, String nombre, String tipo, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}