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
}