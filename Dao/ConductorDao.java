package Dao;

import Modelo.Conductor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConductorDao {

    public boolean insertar(String nombre, String licencia, String telefono) throws SQLException {
        String sql = "INSERT INTO conductores (nombre, licencia, telefono) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, licencia);
            ps.setString(3, telefono);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizar(int id, String nombre, String licencia, String telefono) throws SQLException {
        String sql = "UPDATE conductores SET nombre=?, licencia=?, telefono=? WHERE id=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, licencia);
            ps.setString(3, telefono);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM conductores WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Conductor> obtenerTodos() throws SQLException {
        List<Conductor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, licencia, telefono FROM conductores";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Conductor c = new Conductor();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setLicencia(rs.getString("licencia"));
                c.setTelefono(rs.getString("telefono"));
                lista.add(c);
            }
        }
        return lista;
    }
}
