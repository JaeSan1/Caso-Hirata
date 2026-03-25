package dao;

import java.sql.*;
import modelo.Camion;

public class CamionDao {

    public void registrarKm(int id, double nuevoKm) {
        String sql = "UPDATE camiones SET km_actual = ? WHERE id = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setDouble(1, nuevoKm); 
            ps.setInt(2, id);        
            ps.executeUpdate();       
            
        } catch (SQLException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    public double verKilometraje(int id) {
        String sql = "SELECT km_actual FROM camiones WHERE id = ?";
        double km = 0;

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                km = rs.getDouble("km_actual"); 
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
        return km;
    }
}