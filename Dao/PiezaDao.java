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

    public boolean actualizarStock(int id, int cantidad) throws SQLException {
        String sql = "UPDATE piezas SET stock = stock + ? WHERE id = ?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }
}
