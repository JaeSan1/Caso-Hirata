package Dao;

import Modelo.Pieza;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PiezaDao {

    public List<Pieza> listarPiezas() throws SQLException {
        List<Pieza> lista = new ArrayList<>();
        String sql = "SELECT * FROM piezas";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Pieza(rs.getInt("id"), rs.getString("nombre"), rs.getInt("stock")));
            }
        }
        return lista;
    }

    public List<Object[]> obtenerTodosParaTabla() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, stock FROM piezas";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("stock")
                });
            }
        }
        return lista;
    }

    public boolean insertarPieza(String nombre, int stock) throws SQLException {
        String sql = "INSERT INTO piezas (nombre, stock) VALUES (?, ?)";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, stock);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarPieza(int id, String nombre, int stock) throws SQLException {
        String sql = "UPDATE piezas SET nombre = ?, stock = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, stock);
            ps.setInt(3, id);
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
