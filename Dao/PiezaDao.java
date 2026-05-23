package Dao;

import Modelo.Pieza;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PiezaDao {

    public List<Object[]> obtenerTodosParaTabla() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, stock, fecha_movimiento, estado FROM piezas ORDER BY id DESC";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("stock"),
                    rs.getDate("fecha_movimiento") != null ? rs.getDate("fecha_movimiento").toString() : "Sin Fecha",
                    rs.getString("estado") != null ? rs.getString("estado") : "Excelente"
                });
            }
        }
        return lista;
    }

    public boolean insertarPieza(String nombre, int stock, String fecha, String estado) throws SQLException {
        String sql = "INSERT INTO piezas (nombre, stock, fecha_movimiento, estado) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, stock);
            
            if (fecha == null || fecha.trim().isEmpty() || fecha.contains("YYYY-MM-DD")) {
                ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            } else {
                ps.setDate(3, Date.valueOf(fecha.replace("/", "-")));
            }
            
            ps.setString(4, estado);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarPieza(int id, String nombre, int stock, String fecha, String estado) throws SQLException {
        String sql = "UPDATE piezas SET nombre = ?, stock = ?, fecha_movimiento = ?, estado = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, stock);
            
            if (fecha == null || fecha.trim().isEmpty() || fecha.contains("YYYY-MM-DD")) {
                ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            } else {
                ps.setDate(3, Date.valueOf(fecha.replace("/", "-")));
            }
            
            ps.setString(4, estado);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarStock(int id, int stock, String fecha, String estado) throws SQLException {
        String sql = "UPDATE piezas SET stock = stock + ?, fecha_movimiento = ?, estado = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, stock);
            
            if (fecha == null || fecha.trim().isEmpty() || fecha.contains("YYYY-MM-DD")) {
                ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            } else {
                ps.setDate(2, Date.valueOf(fecha.replace("/", "-")));
            }
            
            ps.setString(3, estado);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarStock(int id, int stock) throws SQLException {
        String sql = "UPDATE piezas SET stock = ?, fecha_movimiento = CURDATE() WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, stock);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarPieza(int id) throws SQLException {
        String sql = "DELETE FROM piezas WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
