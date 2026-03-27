package Dao;

import java.sql.*;
import Modelo.Camion;
import Dao.Conexion;

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

    public boolean eliminar(int idEliminar) throws SQLException {
        String sql = "DELETE FROM camion WHERE id_camion=?";

        try (Connection conn = getConexion(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEliminar);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) { throw e; }
    }
}