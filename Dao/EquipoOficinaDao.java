package Dao;

import Modelo.EquipoOficina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoOficinaDao {

    public List<EquipoOficina> listarEquipos() throws SQLException {
        List<EquipoOficina> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipos_oficina";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new EquipoOficina(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("tipo"),
                    rs.getString("estado")
                ));
            }
        }
        return lista;
    }

    public List<Object[]> obtenerTodosParaTabla() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, tipo, estado FROM equipos_oficina";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("tipo"),
                    rs.getString("estado")
                });
            }
        }
        return lista;
    }

    public boolean insertarEquipo(String nombre, String tipo, String estado) throws SQLException {
        String sql = "INSERT INTO equipos_oficina (nombre, tipo, estado) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, tipo);
            ps.setString(3, estado);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarEquipo(int id, String nombre, String tipo, String estado) throws SQLException {
        String sql = "UPDATE equipos_oficina SET nombre = ?, tipo = ?, estado = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, tipo);
            ps.setString(3, estado);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarEquipo(int id) throws SQLException {
        String sql = "DELETE FROM equipos_oficina WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}