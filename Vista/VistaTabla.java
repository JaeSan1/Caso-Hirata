package Vista;

import Controlador.ControladorFlota;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class VistaTabla extends JFrame {
    // Componentes de interfaz
    private List<JTextField> campos = new ArrayList<>();
    private JTable tablePrincipal, tableAlertas;
    private DefaultTableModel modelPrincipal, modelAlertas;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLeer;
    
    // Controladores
    private String tituloModulo;
    private final ControladorFlota ctrlFlota = new ControladorFlota();

    public VistaTabla(String titulo, String[] columnas, String[] etiquetasForm, boolean esCamion) {
        this.tituloModulo = titulo;
        
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        // CONFIGURACIÓN DE VENTANA
        setTitle(titulo + " - Empresa Hirata");
        setSize(1150, 720); 
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // PANEL PRINCIPAL AMARILLO
        JPanel panelPrincipal = new JPanel(null);
        panelPrincipal.setBounds(0, 0, 1135, 680);
        panelPrincipal.setBackground(Color.decode("#FFFFE0")); 
        add(panelPrincipal);

        // --- PANEL DE DATOS (IZQUIERDA) ---
        JPanel panelDatos = new JPanel(null);
        panelDatos.setBorder(BorderFactory.createTitledBorder(" Registro de Información "));
        panelDatos.setBounds(20, 20, 440, 390);
        panelDatos.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelDatos);

        int y = 25, gap = 31; 
        for (int i = 0; i < etiquetasForm.length; i++) {
            JLabel lbl = new JLabel(etiquetasForm[i] + ":");
            lbl.setBounds(25, y, 110, 25);
            panelDatos.add(lbl);
            
            JTextField txt = new JTextField();
            txt.setBounds(140, y, 280, 25);
            panelDatos.add(txt);
            campos.add(txt);
            
            // Navegación con Enter
            final int index = i;
            txt.addActionListener(e -> {
                if (index < campos.size() - 1) {
                    campos.get(index + 1).requestFocus();
                } else {
                    btnAgregar.doClick();
                }
            });
            y += gap;
        }

        // --- PANEL DE ALERTAS (DERECHA) ---
        if (esCamion) {
            JPanel panelAlertasBox = new JPanel(new BorderLayout());
            panelAlertasBox.setBorder(BorderFactory.createTitledBorder("[ MONITOR DE ALERTAS MANTENIMIENTO ]"));
            panelAlertasBox.setBackground(Color.decode("#FFFFE0"));
            panelAlertasBox.setBounds(480, 20, 630, 390);
            
            modelAlertas = new DefaultTableModel(new String[]{"Vehículo", "Exceso (Km)", "Estado"}, 0);
            tableAlertas = new JTable(modelAlertas);
            estilizarTabla(tableAlertas);
            panelAlertasBox.add(new JScrollPane(tableAlertas), BorderLayout.CENTER);
            panelPrincipal.add(panelAlertasBox);
        }

        // --- PANEL DE BOTONES ---
        JPanel panelOps = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelOps.setBounds(20, 420, 1090, 40);
        panelOps.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelOps);

        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLeer = new JButton("Leer");
        
        panelOps.add(btnAgregar); panelOps.add(btnActualizar); panelOps.add(btnEliminar); panelOps.add(btnLeer);

        // --- TABLA PRINCIPAL (ABAJO) ---
        modelPrincipal = new DefaultTableModel(columnas, 0);
        tablePrincipal = new JTable(modelPrincipal);
        estilizarTabla(tablePrincipal);

        // Ocultar ID
        if (tablePrincipal.getColumnCount() > 0) {
            tablePrincipal.getColumnModel().getColumn(0).setMinWidth(0);
            tablePrincipal.getColumnModel().getColumn(0).setMaxWidth(0);
            tablePrincipal.getColumnModel().getColumn(0).setWidth(0);
        }

        JScrollPane scrollPrincipal = new JScrollPane(tablePrincipal);
        scrollPrincipal.setBounds(20, 470, 1090, 180); 
        panelPrincipal.add(scrollPrincipal);

        configurarListeners();
        cargarDatosDesdeBase();
    }

    private void estilizarTabla(JTable t) {
        t.setRowHeight(25);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JTableHeader header = t.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void configurarListeners() {
        // 1. LEER: Pasa los datos de la fila seleccionada a los JTextField
btnLeer.addActionListener(e -> {
    int fila = tablePrincipal.getSelectedRow();
    if (fila >= 0) {
        try {
            // Recorremos la lista de campos de texto que definimos a la izquierda
            for (int i = 0; i < campos.size(); i++) {
                // Obtenemos el valor de la tabla. 
                // i + 1 asume que la columna 0 es el ID oculto
                Object valor = modelPrincipal.getValueAt(fila, i + 1);
                
                if (valor != null) {
                    campos.get(i).setText(valor.toString());
                } else {
                    campos.get(i).setText("");
                }
            }
        } catch (Exception ex) {
            System.err.println("Error al leer fila: " + ex.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(this, "Seleccione un registro en la tabla para leer.");
    }
});

        // 2. ACTUALIZAR: Toma lo que hay en los campos y actualiza el ID seleccionado
        btnActualizar.addActionListener(e -> {
            int fila = tablePrincipal.getSelectedRow();
            if (fila >= 0) {
                int id = Integer.parseInt(modelPrincipal.getValueAt(fila, 0).toString());
                if (tituloModulo.contains("Camiones")) {
                    ctrlFlota.actualizarCamion(id, campos.get(0).getText(), campos.get(1).getText(), 
                        campos.get(2).getText(), campos.get(3).getText(), campos.get(4).getText(), 
                        modelPrincipal, modelAlertas);
                } else if (tituloModulo.contains("Conductores")) {
                    ctrlFlota.actualizarConductor(id, campos.get(0).getText(), campos.get(1).getText(), 
                        campos.get(2).getText(), modelPrincipal);
                }
                limpiarControles();
            } else {
                JOptionPane.showMessageDialog(this, "Debe 'Leer' un registro primero para actualizarlo.");
            }
        });

        // 3. GUARDAR (NUEVO)
        btnAgregar.addActionListener(e -> {
            if (tituloModulo.contains("Camiones")) {
            ctrlFlota.agregarCamion(campos.get(0).getText(), campos.get(1).getText(), 
            campos.get(2).getText(), campos.get(3).getText(), campos.get(4).getText(), 
            modelPrincipal, modelAlertas);
    } else if (tituloModulo.contains("Conductores")) {
        ctrlFlota.agregarConductor(campos.get(0).getText(), campos.get(1).getText(), 
            campos.get(2).getText(), modelPrincipal);
    } 
    
    else if (tituloModulo.contains("Mantenimientos")) {
        try {
            String fecha = campos.get(0).getText();
            String tipo = campos.get(1).getText();
            String desc = campos.get(2).getText();
            String costo = campos.get(3).getText(); 

            // Para mantenimientos, necesitamos un ID de camión. 
            // Por ahora usaremos el ID 1 para probar que guarde
            int idCamionPrueba = 1; 

            ctrlFlota.agregarMantenimiento(idCamionPrueba, fecha, tipo, desc, modelPrincipal);
            
            cargarDatosDesdeBase(); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar mantenimiento: " + ex.getMessage());
        }
    }
    limpiarControles();
});

        // 4. ELIMINAR
        btnEliminar.addActionListener(e -> {
            int fila = tablePrincipal.getSelectedRow();
            if (fila >= 0) {
                int id = Integer.parseInt(modelPrincipal.getValueAt(fila, 0).toString());
                if (tituloModulo.contains("Camiones")) {
                    ctrlFlota.eliminarCamion(id, modelPrincipal, modelAlertas);
                } else if (tituloModulo.contains("Conductores")) {
                    ctrlFlota.eliminarConductor(id, modelPrincipal);
                }
                limpiarControles();
            }
        });
    }

    private void cargarDatosDesdeBase() {
    try {
        modelPrincipal.setRowCount(0);
        if (modelAlertas != null) modelAlertas.setRowCount(0);

        if (tituloModulo.contains("Camiones")) {
            ctrlFlota.cargarCamiones(modelPrincipal, modelAlertas);
        } else if (tituloModulo.contains("Conductores")) {
            ctrlFlota.cargarConductores(modelPrincipal);
        } else if (tituloModulo.contains("Mantenimientos")) { 
            ctrlFlota.cargarHistorial(modelPrincipal); 
        }

        tablePrincipal.revalidate();
        tablePrincipal.repaint();
    } catch (Exception e) {
        System.err.println("Error al cargar: " + e.getMessage());
    }
}

    private void limpiarControles() {
        for (JTextField f : campos) f.setText("");
        tablePrincipal.clearSelection();
    }
}