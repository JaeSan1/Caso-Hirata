package Dao;

import Modelo.Camion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CamionDao {
    
    // Método para insertar solo el camión (Ventana 1)
    public boolean insertarSoloCamion(Camion camion) throws SQLException {
        String sql = "INSERT INTO camiones (marca, modelo, anio, km_actual, km_ultimo_mantenimiento) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, camion.getMarca());
            ps.setString(2, camion.getModelo());
            ps.setInt(3, camion.getAnio());
            ps.setDouble(4, camion.getKmActual());
            ps.setDouble(5, camion.getKmUltimoMantenimiento());
            return ps.executeUpdate() > 0;
        }
    }

    // Método para actualizar solo el camión
    public boolean actualizarSoloCamion(Camion camion) throws SQLException {
        String sql = "UPDATE camiones SET marca=?, modelo=?, anio=?, km_actual=?, km_ultimo_mantenimiento=? WHERE id=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, camion.getMarca());
            ps.setString(2, camion.getModelo());
            ps.setInt(3, camion.getAnio());
            ps.setDouble(4, camion.getKmActual());
            ps.setDouble(5, camion.getKmUltimoMantenimiento());
            ps.setInt(6, camion.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // Obtener datos simplificados para la tabla
    public List<Object[]> obtenerSoloCamiones() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id, marca, modelo, anio, km_actual, km_ultimo_mantenimiento FROM camiones ORDER BY id DESC";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id"), rs.getString("marca"), rs.getString("modelo"),
                    rs.getInt("anio"), rs.getDouble("km_actual"), rs.getDouble("km_ultimo_mantenimiento")
                });
            }
        }
        return lista;
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM camiones WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}