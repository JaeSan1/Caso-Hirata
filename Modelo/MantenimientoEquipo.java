package Modelo;
import java.sql.Date;

public class MantenimientoEquipo {
    private int id;
    private int equipoId;
    private Date fecha;
    private String tipo; // Preventivo o Correctivo
    private String descripcion;

    public MantenimientoEquipo() {}

    public MantenimientoEquipo(int id, int equipoId, Date fecha, String tipo, String descripcion) {
        this.id = id;
        this.equipoId = equipoId;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEquipoId() { return equipoId; }
    public void setEquipoId(int equipoId) { this.equipoId = equipoId; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}