package Modelo;
import java.sql.Date;

public class SoftwareEquipo {
    private int id;
    private int equipoId;
    private String nombreSoftware;
    private String version;
    private Date fechaActualizacion;

    public SoftwareEquipo() {}

    // Constructor  
    public SoftwareEquipo(int id, int equipoId, String nombreSoftware, String version, Date fechaActualizacion) {
        this.id = id;
        this.equipoId = equipoId;
        this.nombreSoftware = nombreSoftware;
        this.version = version;
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEquipoId() { return equipoId; }
    public void setEquipoId(int equipoId) { this.equipoId = equipoId; }
    public String getNombreSoftware() { return nombreSoftware; }
    public void setNombreSoftware(String nombreSoftware) { this.nombreSoftware = nombreSoftware; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public Date getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Date fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}