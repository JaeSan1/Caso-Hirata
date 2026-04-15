package Dao;

import Modelo.Camion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CamionDao {

    // Método privado para convertir DD-MM-AAAA (Vista) a AAAA-MM-DD
    private String formatearParaDB(String fechaVista) {
        try {
            if (fechaVista == null || fechaVista.contains("D") || fechaVista.isEmpty()) return null;
            String[] partes = fechaVista.split("-");

            // Convierte de DD-MM-AAAA a AAAA-MM-DD
            return partes[2] + "-" + partes[1] + "-" + partes[0];
        } catch (Exception e) {
            return null;
        }
    }

    // Método privado para convertir AAAA-MM-DD (MySQL) a DD-MM-AAAA (Vista)
    private String formatearParaVista(String fechaDB) {
        try {
            if (fechaDB == null) return "DD-MM-AAAA";
            String[] partes = fechaDB.split("-");

            // Convierte de AAAA-MM-DD a DD-MM-AAAA
            return partes[2] + "-" + partes[1] + "-" + partes[0];
        } catch (Exception e) {
            return "DD-MM-AAAA";
        }
    }

    // Insertamos datos de un nuevo camión en la base de datos
    public boolean insertar(Camion camion) throws SQLException {
        String sql = "INSERT INTO camiones (marca, modelo, anio, km_actual, km_ultimo_mantenimiento, fecha_ultimo_mantenimiento, conductor_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, camion.getMarca());
            ps.setString(2, camion.getModelo());
            ps.setInt(3, camion.getAnio());
            ps.setDouble(4, camion.getKmActual());
            ps.setDouble(5, camion.getKmUltimoMantenimiento());
            ps.setString(6, formatearParaDB(camion.getFechaUltimoMantenimiento()));
            ps.setInt(7, camion.getConductorId());
            
            return ps.executeUpdate() > 0;
        }
    }

    public List<Camion> obtenerTodos() throws SQLException {
        List<Camion> lista = new ArrayList<>();
        String sql = "SELECT * FROM camiones";

        try (Connection con = Conexion.getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Camion c = new Camion();
                c.setId(rs.getInt("id"));
                c.setMarca(rs.getString("marca"));
                c.setModelo(rs.getString("modelo"));
                c.setAnio(rs.getInt("anio"));
                c.setKmActual(rs.getDouble("km_actual"));
                c.setKmUltimoMantenimiento(rs.getDouble("km_ultimo_mantenimiento"));
                c.setFechaUltimoMantenimiento(formatearParaVista(rs.getString("fecha_ultimo_mantenimiento")));
                c.setConductorId(rs.getInt("conductor_id"));
                lista.add(c);
            }
        }
        return lista;
    }

    public boolean actualizar(Camion camion) throws SQLException {
        String sql = "UPDATE camiones SET marca=?, modelo=?, anio=?, km_actual=?, km_ultimo_mantenimiento=?, fecha_ultimo_mantenimiento=?, conductor_id=? WHERE id=?";

        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, camion.getMarca());
            ps.setString(2, camion.getModelo());
            ps.setInt(3, camion.getAnio());
            ps.setDouble(4, camion.getKmActual());
            ps.setDouble(5, camion.getKmUltimoMantenimiento());
            ps.setString(6, formatearParaDB(camion.getFechaUltimoMantenimiento()));
            ps.setInt(7, camion.getConductorId());
            ps.setInt(8, camion.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM camiones WHERE id = ?";

        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Lógica para la Alerta de Mantenimiento
    public boolean necesitaMantenimiento(int id) throws SQLException {
        String sql = "SELECT km_actual, km_ultimo_mantenimiento FROM camiones WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double actual = rs.getDouble("km_actual");
                double ultimo = rs.getDouble("km_ultimo_mantenimiento");
                return (actual - ultimo) >= 5000;
            }
        }
        return false;
    }
}