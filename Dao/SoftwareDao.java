package Dao;

import Modelo.SoftwareEquipo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SoftwareDao {

    public boolean registrarSoftware(SoftwareEquipo soft) throws SQLException {
        String sql = "INSERT INTO software_equipos (equipo_id, nombre_software, version, fecha_actualizacion) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, soft.getEquipoId());
            ps.setString(2, soft.getNombreSoftware());
            ps.setString(3, soft.getVersion());
            ps.setDate(4, soft.getFechaActualizacion());
            return ps.executeUpdate() > 0;
        }
    }

    public List<SoftwareEquipo> listarSoftwarePorEquipo(int equipoId) throws SQLException {
        List<SoftwareEquipo> lista = new ArrayList<>();
        String sql = "SELECT * FROM software_equipos WHERE equipo_id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, equipoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new SoftwareEquipo(
                        rs.getInt("id"),
                        rs.getInt("equipo_id"),
                        rs.getString("nombre_software"),
                        rs.getString("version"),
                        rs.getDate("fecha_actualizacion")
                    ));
                }
            }
        }
        return lista;
    }

    public List<Object[]> obtenerTodosParaTabla() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id, equipo_id, nombre_software, version FROM software_equipos";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id"),
                    rs.getInt("equipo_id"),
                    rs.getString("nombre_software"),
                    rs.getString("version")
                });
            }
        }
        return lista;
    }

    public boolean insertarSoftware(int equipoId, String nombreSoftware, String version) throws SQLException {
        // Verificar que el equipo existe
        String sqlCheck = "SELECT COUNT(*) FROM equipos_oficina WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
            psCheck.setInt(1, equipoId);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new SQLException("El equipo con ID " + equipoId + " no existe en la base de datos.");
                }
            }
        }
        
        String sql = "INSERT INTO software_equipos (equipo_id, nombre_software, version, fecha_actualizacion) VALUES (?, ?, ?, CURDATE())";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, equipoId);
            ps.setString(2, nombreSoftware);
            ps.setString(3, version);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarSoftware(int id, int equipoId, String nombreSoftware, String version) throws SQLException {
        // Verificar que el equipo existe
        String sqlCheck = "SELECT COUNT(*) FROM equipos_oficina WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
            psCheck.setInt(1, equipoId);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new SQLException("El equipo con ID " + equipoId + " no existe en la base de datos.");
                }
            }
        }
        
        String sql = "UPDATE software_equipos SET equipo_id = ?, nombre_software = ?, version = ?, fecha_actualizacion = CURDATE() WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, equipoId);
            ps.setString(2, nombreSoftware);
            ps.setString(3, version);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarSoftware(int id) throws SQLException {
        String sql = "DELETE FROM software_equipos WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}