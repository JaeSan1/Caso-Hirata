package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoDao {

    public boolean insertarMantenimiento(int idCamion, String fecha, String tipo, String desc, double km) {
    String sql = "INSERT INTO mantenimientos (id_camion, fecha, tipo_mantenimiento, descripcion, kilometraje) VALUES (?, ?, ?, ?, ?)";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setInt(1, idCamion);
        ps.setString(2, fecha);
        ps.setString(3, tipo);
        ps.setString(4, desc);
        ps.setDouble(5, km); // Aquí es donde entra el kilometraje
        
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    public List<Object[]> obtenerHistorialCompleto() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT m.id, c.marca, m.fecha, m.tipo, m.descripcion FROM mantenimientos m " +
                    "JOIN camiones c ON m.camion_id = c.id ORDER BY m.fecha DESC";
        try (Connection con = Conexion.getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id"), rs.getString("marca"), rs.getString("fecha"),
                    rs.getString("tipo"), rs.getString("descripcion")
                });
            }
        }
        return lista;
    }

    public boolean actualizarMantenimiento(int idMant, String fecha, String tipo, String desc, double km) {
    String sql = "UPDATE mantenimientos SET fecha=?, tipo_mantenimiento=?, descripcion=?, kilometraje=? WHERE id_mantenimiento=?";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, fecha);
        ps.setString(2, tipo);
        ps.setString(3, desc);
        ps.setDouble(4, km);
        ps.setInt(5, idMant);
        
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM mantenimientos WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
