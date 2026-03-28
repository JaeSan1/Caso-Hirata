package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Modelo.Conductor;

public class ConductorDao {

    // Método para obtener todos los conductores (Necesario para llenar JComboBox)
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
        } catch (SQLException e) {
            System.err.println("Error al obtener conductores: " + e.getMessage());
            throw e;
        }
        return lista;
    }

    // Método para buscar un nombre de conductor por su ID (Útil para la tabla de Camiones)
    public String obtenerNombrePorId(int id) throws SQLException {
        String sql = "SELECT nombre FROM conductores WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
        }
        return "Sin conductor";
    }
}