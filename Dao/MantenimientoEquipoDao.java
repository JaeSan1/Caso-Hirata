package Dao;

import Modelo.MantenimientoEquipo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoEquipoDao {

    public boolean registrarMantenimiento(MantenimientoEquipo mant) throws SQLException {
        String sql = "INSERT INTO mantenimientos_equipos (equipo_id, fecha, tipo, descripcion) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mant.getEquipoId());
            ps.setDate(2, mant.getFecha());
            ps.setString(3, mant.getTipo());
            ps.setString(4, mant.getDescripcion());
            return ps.executeUpdate() > 0;
        }
    }

    public List<MantenimientoEquipo> consultarHistorial(int equipoId) throws SQLException {
        List<MantenimientoEquipo> historial = new ArrayList<>();
        String sql = "SELECT * FROM mantenimientos_equipos WHERE equipo_id = ? ORDER BY fecha DESC";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, equipoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    historial.add(new MantenimientoEquipo(
                        rs.getInt("id"),
                        rs.getInt("equipo_id"),
                        rs.getDate("fecha"),
                        rs.getString("tipo"),
                        rs.getString("descripcion")
                    ));
                }
            }
        }
        return historial;
    }

    public List<Object[]> obtenerTodosParaTabla() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id, equipo_id, fecha, tipo, descripcion FROM mantenimientos_equipos";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id"),
                    rs.getInt("equipo_id"),
                    rs.getDate("fecha"),
                    rs.getString("tipo"),
                    rs.getString("descripcion")
                });
            }
        }
        return lista;
    }

    public boolean insertarMantenimiento(int equipoId, String fecha, String tipo, String descripcion) throws SQLException {
        String sql = "INSERT INTO mantenimientos_equipos (equipo_id, fecha, tipo, descripcion) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, equipoId);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setString(3, tipo);
            ps.setString(4, descripcion);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarMantenimiento(int id, int equipoId, String fecha, String tipo, String descripcion) throws SQLException {
        String sql = "UPDATE mantenimientos_equipos SET equipo_id = ?, fecha = ?, tipo = ?, descripcion = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, equipoId);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setString(3, tipo);
            ps.setString(4, descripcion);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarMantenimiento(int id) throws SQLException {
        String sql = "DELETE FROM mantenimientos_equipos WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}