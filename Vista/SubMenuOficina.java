package Vista;

import java.awt.*;
import javax.swing.*;

public class SubMenuOficina extends JFrame {
    public SubMenuOficina() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        setTitle("Hirata - Submenú Gestión de Oficina");
        setSize(400, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 15, 15));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        getContentPane().setBackground(Color.decode("#FFFFE0")); // Fondo amarillo Hirata

        JButton btnEquipos = new JButton("1. Inventario equipos");
        JButton btnSoftware = new JButton("2. Softwares");
        JButton btnPiezas = new JButton("3. Stock de Repuestos");
        JButton btnMantenimiento = new JButton("4. Mantenimiento");
        JButton btnVolver = new JButton("Volver");

        // 1. EQUIPOS
        btnEquipos.addActionListener(e -> {
            String[] col = {"ID", "Nombre", "Tipo", "Estado"};
            String[] form = {"Nombre Equipo", "Tipo (PC/Impresora)", "Estado"};
            new VistaTabla("Inventario de Equipos", col, form, false).setVisible(true);
        });

        // 2. SOFTWARE 
        btnSoftware.addActionListener(e -> {
            String[] col = {"ID", "ID Equipo", "Programa", "Versión"};
            String[] form = {"ID Equipo", "Nombre Software", "Versión Actual"};
            new VistaTabla("Gestión de Software", col, form, false).setVisible(true);
        });

        // 3. PIEZAS
        btnPiezas.addActionListener(e -> {
            String[] col = {"ID", "Nombre Pieza", "Stock"};
            String[] form = {"Nombre de Pieza", "Cantidad en Stock"};
            new VistaTabla("Inventario de Repuestos TI", col, form, false).setVisible(true);
        });

        // 4. MANTENIMIENTO 
        btnMantenimiento.addActionListener(e -> {
            String[] col = {"ID", "ID Equipo", "Fecha", "Tipo", "Descripción"};
            String[] form = {"ID Equipo", "Fecha Servicio", "Tipo (Preventivo/Correctivo)", "Descripción"};
            new VistaTabla("Mantenimiento de Oficina", col, form, false).setVisible(true);
        });

        btnVolver.addActionListener(e -> { 
            new MenuPrincipal().setVisible(true); 
            this.dispose(); 
        });

        add(btnEquipos); add(btnSoftware); add(btnPiezas); add(btnMantenimiento); add(btnVolver);
    }
}