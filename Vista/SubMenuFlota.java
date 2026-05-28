package Vista;

import java.awt.*;
import javax.swing.*;

public class SubMenuFlota extends JFrame {
    public SubMenuFlota() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        setTitle("Gestión de Flota - Empresa Hirata");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 15, 15));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        getContentPane().setBackground(Color.decode("#FFFFE0"));

        JButton btnCamiones = new JButton("1. Camiones");
        JButton btnConductores = new JButton("2. Conductores");
        JButton btnMantenimientos = new JButton("3. Mantenimientos");
        JButton btnVolver = new JButton(" Volver");

        //CAMIONES
        btnCamiones.addActionListener(e -> {
            String[] col = {"ID", "Marca", "Modelo", "Año", "KM Actual", "KM Últ. Mant"};
            String[] form = {"Marca", "Modelo", "Año", "KM Actual", "KM Últ. Mant"};
            new VistaTabla("Gestión de Camiones", col, form, true).setVisible(true);
        });

        // CONDUCTORES
        btnConductores.addActionListener(e -> {
            String[] col = {"ID", "Nombre", "Licencia", "Teléfono"};
            String[] form = {"Nombre Completo", "Licencia", "Teléfono"};
            new VistaTabla("Registro de Conductores", col, form, false).setVisible(true);
        });

        // MANTENIMIENTOS 
        btnMantenimientos.addActionListener(e -> {
            String[] col = {"ID", "Marca Camión", "Fecha", "Tipo Mant.", "Descripción", "KM al Momento"};
            String[] form = {"ID del Camión", "Fecha (YYYY-MM-DD)", "Tipo Mant.", "Descripción", "KM al Momento"};
            new VistaTabla("Historial de Mantenimientos", col, form, false).setVisible(true);
        });

        btnVolver.addActionListener(e -> { 
            new MenuPrincipal().setVisible(true); 
            this.dispose(); 
        });

        add(btnCamiones); add(btnConductores); add(btnMantenimientos); add(btnVolver);
    }
}
