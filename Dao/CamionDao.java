package Dao;

import Modelo.Camion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CamionDao {

    // FORMATEO DE FECHA 
    private String formatearParaDB(String fechaVista) {
        try {
            if (fechaVista == null || fechaVista.contains("D") || fechaVista.isEmpty()) return null;
            String[] partes = fechaVista.split("-");
            return partes[2] + "-" + partes[1] + "-" + partes[0]; // DD-MM-AAAA -> AAAA-MM-DD
        } catch (Exception e) { return null; }
    }

    private String formatearParaVista(String fechaDB) {
        try {
            if (fechaDB == null) return "DD-MM-AAAA";
            String[] partes = fechaDB.split("-");
            return partes[2] + "-" + partes[1] + "-" + partes[0]; // AAAA-MM-DD -> DD-MM-AAAA
        } catch (Exception e) { return "DD-MM-AAAA"; }
    }

    // INSERTAR 
    public boolean insertar(Camion camion, String tipoMant, String descMant, String nombreCond, String licencia, String telefono) throws SQLException {
        String sqlCamion = "INSERT INTO camiones (marca, modelo, anio, km_actual, km_ultimo_mantenimiento, fecha_ultimo_mantenimiento, conductor_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlMant = "INSERT INTO mantenimientos (camion_id, fecha, tipo, descripcion, km_al_momento) VALUES (?, ?, ?, ?, ?)";
        String sqlCond = "INSERT INTO conductores (nombre, licencia, telefono) VALUES (?, ?, ?)";

        Connection con = null;
        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false);

            // Insertar Conductor 
            PreparedStatement psCond = con.prepareStatement(sqlCond, Statement.RETURN_GENERATED_KEYS);
            psCond.setString(1, nombreCond);
            psCond.setString(2, licencia);
            psCond.setString(3, telefono);
            psCond.executeUpdate();
            ResultSet rsCond = psCond.getGeneratedKeys();
            int idCond = rsCond.next() ? rsCond.getInt(1) : 1;

            // Insertar Camión 
            PreparedStatement psCam = con.prepareStatement(sqlCamion, Statement.RETURN_GENERATED_KEYS);
            psCam.setString(1, camion.getMarca());
            psCam.setString(2, camion.getModelo());
            psCam.setInt(3, camion.getAnio());
            psCam.setDouble(4, camion.getKmActual());
            psCam.setDouble(5, camion.getKmUltimoMantenimiento());
            psCam.setString(6, formatearParaDB(camion.getFechaUltimoMantenimiento()));
            psCam.setInt(7, idCond);
            psCam.executeUpdate();
            ResultSet rsCam = psCam.getGeneratedKeys();
            int idCam = rsCam.next() ? rsCam.getInt(1) : 0;

            // Insertar Mantenimiento 
            PreparedStatement psMant = con.prepareStatement(sqlMant);
            psMant.setInt(1, idCam);
            psMant.setString(2, formatearParaDB(camion.getFechaUltimoMantenimiento()));
            psMant.setString(3, tipoMant);
            psMant.setString(4, descMant);
            psMant.setDouble(5, camion.getKmUltimoMantenimiento());
            psMant.executeUpdate();

            con.commit(); // Guarda todo si no hubo errores
            return true;
        } catch (SQLException e) {
            if (con != null) con.rollback(); // Deshace todo en caso de error
            throw e;
        } finally {
            if (con != null) con.close(); // Cierra conexión siempre
        }
    }

    // LEER
    public List<Object[]> obtenerTodos() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT c.id, c.marca, c.modelo, c.anio, c.km_actual, c.km_ultimo_mantenimiento, " +
                    "c.fecha_ultimo_mantenimiento, m.tipo, m.descripcion, " +
                    "cond.nombre, cond.licencia, cond.telefono " +
                    "FROM camiones c " +
                    "LEFT JOIN mantenimientos m ON c.id = m.camion_id " +
                    "LEFT JOIN conductores cond ON c.conductor_id = cond.id " +
                    "GROUP BY c.id ORDER BY c.id DESC";

        try (Connection con = Conexion.getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getInt("anio"),
                    rs.getDouble("km_actual"),
                    rs.getDouble("km_ultimo_mantenimiento"),
                    formatearParaVista(rs.getString("fecha_ultimo_mantenimiento")),
                    rs.getString("tipo"),
                    rs.getString("descripcion"),
                    rs.getString("nombre"),
                    rs.getString("licencia"),
                    rs.getString("telefono")
                });
            }
        }
        return lista;
    }

    // ACTUALIZAR 
    public boolean actualizar(Camion camion, String tipoMant, String descMant, String nombreCond, String licencia, String telefono) throws SQLException {
    String sqlCamion = "UPDATE camiones SET marca=?, modelo=?, anio=?, km_actual=?, km_ultimo_mantenimiento=?, fecha_ultimo_mantenimiento=? WHERE id=?";
    
    //Mantenimiento: Si no existe el registro se crea.
    String sqlMant = "INSERT INTO mantenimientos (camion_id, tipo, descripcion, fecha, km_al_momento) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE tipo=VALUES(tipo), descripcion=VALUES(descripcion), fecha=VALUES(fecha)";

    //Actualiza el conductor 
    String sqlCond = "UPDATE conductores SET nombre=?, licencia=?, telefono=? WHERE id=(SELECT conductor_id FROM camiones WHERE id=?)";

    Connection con = null;
    try {
        con = Conexion.getConexion();
        con.setAutoCommit(false); // seguridad

        // Actualizar Camión
        PreparedStatement psC = con.prepareStatement(sqlCamion);
        psC.setString(1, camion.getMarca());
        psC.setString(2, camion.getModelo());
        psC.setInt(3, camion.getAnio());
        psC.setDouble(4, camion.getKmActual());
        psC.setDouble(5, camion.getKmUltimoMantenimiento());
        psC.setString(6, formatearParaDB(camion.getFechaUltimoMantenimiento()));
        psC.setInt(7, camion.getId());
        psC.executeUpdate();

        // Actualizar Mantenimiento (si no existe, se inserta)
        PreparedStatement psM = con.prepareStatement(sqlMant);
        psM.setInt(1, camion.getId());
        psM.setString(2, tipoMant);
        psM.setString(3, descMant);
        psM.setString(4, formatearParaDB(camion.getFechaUltimoMantenimiento()));
        psM.setDouble(5, camion.getKmActual());
        psM.executeUpdate();

        // Actualizar Conductor
        PreparedStatement psCond = con.prepareStatement(sqlCond);
        psCond.setString(1, nombreCond);
        psCond.setString(2, licencia);
        psCond.setString(3, telefono);
        psCond.setInt(4, camion.getId());
        psCond.executeUpdate();

        con.commit(); // Confirmamos todos los cambios
        return true;
    } catch (SQLException e) {
        if (con != null) con.rollback(); // Si falla, volvemos atrás
        throw e;
    } finally {
        if (con != null) con.close();
    }
}

    // ELIMINAR 
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM camiones WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // LÓGICA DE ALERTA
    public boolean necesitaMantenimiento(int id) throws SQLException {
        String sql = "SELECT km_actual, km_ultimo_mantenimiento FROM camiones WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double actual = rs.getDouble("km_actual");
                    double ultimo = rs.getDouble("km_ultimo_mantenimiento");
                    return (actual - ultimo) >= 5000; 
                }
            }
        }
        return false;
    }
}