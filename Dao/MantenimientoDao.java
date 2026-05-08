package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoDao {

    public boolean insertarMantenimiento(int idCamion, String fecha, String tipo, String desc) throws SQLException {
        String sql = "INSERT INTO mantenimientos (camion_id, fecha, tipo, descripcion) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCamion);
            ps.setString(2, fecha); // Formato AAAA-MM-DD
            ps.setString(3, tipo);
            ps.setString(4, desc);
            return ps.executeUpdate() > 0;
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

    public boolean actualizarMantenimiento(int idMant, String fecha, String tipo, String desc) throws SQLException {
    // SQL para actualizar un registro específico por su ID
        String sql = "UPDATE mantenimientos SET fecha = ?, tipo = ?, descripcion = ? WHERE id = ?";
    
            try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
        
            ps.setString(1, fecha); // Formato AAAA-MM-DD
            ps.setString(2, tipo);
            ps.setString(3, desc);
            ps.setInt(4, idMant);
        
            return ps.executeUpdate() > 0;
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
