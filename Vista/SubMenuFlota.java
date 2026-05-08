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

        JButton btnCamiones = new JButton("1. Monitoreo de Camiones (Alertas)");
        JButton btnConductores = new JButton("2. Registro de Personal (Conductores)");
        JButton btnMantenimientos = new JButton("3. Historial de Servicios");
        JButton btnVolver = new JButton("⬅ Volver");

        // VENTANA CAMIONES: Solo datos del vehículo y mantenimiento
        btnCamiones.addActionListener(e -> {
            String[] col = {"ID", "Marca", "Modelo", "Año", "KM Actual", "KM Últ. Mant"};
            String[] form = {"Marca", "Modelo", "Año", "KM Actual", "KM Últ. Mant"};
            new VistaTabla("Gestión de Camiones", col, form, true).setVisible(true);
        });

        // VENTANA CONDUCTORES: Solo datos personales
        btnConductores.addActionListener(e -> {
            String[] col = {"ID", "Nombre", "Licencia", "Teléfono"};
            String[] form = {"Nombre Completo", "Licencia", "Teléfono"};
            new VistaTabla("Registro de Conductores", col, form, false).setVisible(true);
        });

        // VENTANA HISTORIAL: Detalles del servicio
        btnMantenimientos.addActionListener(e -> {
            String[] col = {"ID", "Fecha", "Tipo Mant.", "Descripción", "Costo"};
            String[] form = {"Fecha (DD-MM-YYYY)", "Tipo Mant.", "Descripción", "Costo"};
            new VistaTabla("Historial de Mantenimientos", col, form, false).setVisible(true);
        });

        btnVolver.addActionListener(e -> { 
            new MenuPrincipal().setVisible(true); 
            this.dispose(); 
        });

        add(btnCamiones); add(btnConductores); add(btnMantenimientos); add(btnVolver);
    }
}
