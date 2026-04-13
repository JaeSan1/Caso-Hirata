package Dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

// Local con XAMPP
    private static final String URL = "jdbc:mysql://localhost:3306/hirata_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASS = ""; 

    /* private static final String URL = "jdbc:mysql://10.51.0.89/hirata_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "estudiante"; 
    private static final String PASS = "Net_Dev_#02";    */ 

    public static Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
        return con;
    }
} 