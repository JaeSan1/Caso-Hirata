package Dao;

import Modelo.EquipoOficina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoOficinaDao {

    public List<EquipoOficina> listarEquipos() throws SQLException {
        List<EquipoOficina> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipos_oficina";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new EquipoOficina(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("tipo"),
                    rs.getString("estado")
                ));
            }
        }
        return lista;
    }

    public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE equipos_oficina SET estado = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }
}