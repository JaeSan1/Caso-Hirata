package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Modelo.Camion;

public class CamionDao {

    // RF-01: Insertar datos de un nuevo camión
    public boolean insertar(Camion camion) throws SQLException {
        String sql = "INSERT INTO camiones (marca, modelo, anio, km_actual, km_ultimo_mantenimiento, conductor_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, camion.getMarca());
            ps.setString(2, camion.getModelo());
            ps.setInt(3, camion.getAnio());
            ps.setDouble(4, camion.getKmActual());
            ps.setDouble(5, 0.0); // Inicia en 0 para el primer mantenimiento
            ps.setInt(6, camion.getConductorId());
            
            return ps.executeUpdate() > 0;
        }
    }

    // RF-02: Visualizar información de la flota (Listar)
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
                lista.add(c);
            }
        }
        return lista;
    }

    // RF-02: Actualizar datos (Kilometraje o información)
    public boolean actualizar(Camion camion) throws SQLException {
        String sql = "UPDATE camiones SET marca=?, modelo=?, anio=?, km_actual=?, conductor_id=? WHERE id=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, camion.getMarca());
            ps.setString(2, camion.getModelo());
            ps.setInt(3, camion.getAnio());
            ps.setDouble(4, camion.getKmActual());
            ps.setInt(5, camion.getConductorId());
            ps.setInt(6, camion.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // RF-02: Eliminar un camión del registro
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM camiones WHERE id = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // EXTRA: Lógica para la Alerta de Mantenimiento (RF-01)
    public boolean necesitaMantenimiento(int id) throws SQLException {
        String sql = "SELECT km_actual, km_ultimo_mantenimiento FROM camiones WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double actual = rs.getDouble("km_actual");
                double ultimo = rs.getDouble("km_ultimo_mantenimiento");
                // Si la diferencia es de 5000 o más, retorna true 
                return (actual - ultimo) >= 5000;
            }
        }
        return false;
    }
}