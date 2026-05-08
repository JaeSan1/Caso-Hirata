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
    
    public boolean actualizarVersion(int id, String nuevaVersion, Date nuevaFecha) throws SQLException {
        String sql = "UPDATE software_equipos SET version = ?, fecha_actualizacion = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevaVersion);
            ps.setDate(2, nuevaFecha);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        }
    }
}