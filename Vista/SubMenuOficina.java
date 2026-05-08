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

        JButton btnEquipos = new JButton("1. Inventario de Equipos");
        JButton btnSoftware = new JButton("2. Software y Actualizaciones");
        JButton btnPiezas = new JButton("3. Stock de Repuestos/Insumos");
        JButton btnMantenimiento = new JButton("4. Registro Mantenimiento TI");
        JButton btnVolver = new JButton("⬅ Volver al Menú Principal");

        // 1. EQUIPOS
        btnEquipos.addActionListener(e -> {
            String[] col = {"ID", "Nombre", "Tipo", "Estado", "Ubicación"};
            String[] form = {"Nombre Equipo", "Tipo (PC/Impresora)", "Estado", "Departamento/Oficina"};
            new VistaTabla("Inventario de Equipos", col, form, false).setVisible(true);
        });

        // 2. SOFTWARE (Corregido: ahora abre)
        btnSoftware.addActionListener(e -> {
            String[] col = {"ID", "ID Equipo", "Programa", "Versión", "Licencia"};
            String[] form = {"ID Equipo", "Nombre Software", "Versión Actual", "Clave de Licencia"};
            new VistaTabla("Gestión de Software", col, form, false).setVisible(true);
        });

        // 3. PIEZAS
        btnPiezas.addActionListener(e -> {
            String[] col = {"ID", "Nombre Pieza", "Stock", "Proveedor"};
            String[] form = {"Nombre de Pieza", "Cantidad en Stock", "Nombre Proveedor"};
            new VistaTabla("Inventario de Repuestos TI", col, form, false).setVisible(true);
        });

        // 4. MANTENIMIENTO TI (Corregido: ahora abre)
        btnMantenimiento.addActionListener(e -> {
            String[] col = {"ID", "ID Equipo", "Fecha", "Técnico", "Detalle"};
            String[] form = {"ID Equipo", "Fecha Servicio", "Nombre Técnico", "Acción Realizada"};
            new VistaTabla("Mantenimiento de Oficina", col, form, false).setVisible(true);
        });

        btnVolver.addActionListener(e -> { 
            new MenuPrincipal().setVisible(true); 
            this.dispose(); 
        });

        add(btnEquipos); add(btnSoftware); add(btnPiezas); add(btnMantenimiento); add(btnVolver);
    }
}