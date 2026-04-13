package Vista;

import Controlador.ControladorCamion;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Vista extends JFrame {

    // Componentes de la interfaz
    private JTextField txtMarca, txtModelo, txtAnio, txtKmActual, txtKmMantenimiento, txtFechaMant, txtConductorId;
    private JTable tableAlertas; 
    private DefaultTableModel modelAlertas;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLeer;
    private JTable tablePrincipal;
    private DefaultTableModel modelPrincipal;
    
    private ControladorCamion controlador;

    public Vista() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        configuracion();
        agregarListeners();
        cargarCamiones();
        this.setVisible(true);
    }

    private void configuracion() {
        controlador = new ControladorCamion();
        setTitle("Gestión de Flota - Empresa Hirata");
        setSize(1000, 750); 
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(null);
        panelPrincipal.setBounds(0, 0, 985, 710);
        panelPrincipal.setBackground(Color.decode("#FFFFE0"));
        add(panelPrincipal);

        // --- PANEL DE DATOS ---
        JPanel panelDatos = new JPanel(null);
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Camión"));
        panelDatos.setBounds(20, 40, 440, 310);
        panelDatos.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelDatos);

        int y = 30, gap = 35;
        
        panelDatos.add(new JLabel("Marca:")).setBounds(25, y, 100, 25);
        txtMarca = new JTextField(); txtMarca.setBounds(130, y, 280, 25);
        panelDatos.add(txtMarca);

        panelDatos.add(new JLabel("Modelo:")).setBounds(25, y + gap, 100, 25);
        txtModelo = new JTextField(); txtModelo.setBounds(130, y + gap, 280, 25);
        panelDatos.add(txtModelo);

        panelDatos.add(new JLabel("Año:")).setBounds(25, y + (gap * 2), 100, 25);
        txtAnio = new JTextField(); txtAnio.setBounds(130, y + (gap * 2), 280, 25);
        panelDatos.add(txtAnio);

        panelDatos.add(new JLabel("ID Cond.:")).setBounds(25, y + (gap * 3), 100, 25);
        txtConductorId = new JTextField(); txtConductorId.setBounds(130, y + (gap * 3), 280, 25);
        panelDatos.add(txtConductorId);

        panelDatos.add(new JLabel("KM Actual:")).setBounds(25, y + (gap * 4), 100, 25);
        txtKmActual = new JTextField(); txtKmActual.setBounds(130, y + (gap * 4), 280, 25);
        panelDatos.add(txtKmActual);

        panelDatos.add(new JLabel("KM Últ. Mant.:")).setBounds(25, y + (gap * 5), 100, 25);
        txtKmMantenimiento = new JTextField(); txtKmMantenimiento.setBounds(130, y + (gap * 5), 280, 25);
        panelDatos.add(txtKmMantenimiento);

        panelDatos.add(new JLabel("Fecha Mant.:")).setBounds(25, y + (gap * 6), 100, 25);
        txtFechaMant = new JTextField("DD-MM-AAAA"); 
        txtFechaMant.setBounds(130, y + (gap * 6), 280, 25);
        panelDatos.add(txtFechaMant);

        // --- PANEL DE ALERTAS ---
        JPanel panelAlertas = new JPanel(new BorderLayout());
        panelAlertas.setBorder(BorderFactory.createTitledBorder("[ PANEL DE ALERTAS MANTENIMIENTO ]"));
        panelAlertas.setBounds(480, 40, 480, 310);
        panelAlertas.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelAlertas);

        modelAlertas = new DefaultTableModel(new String[]{"ID", "Marca", "Modelo", "Exceso (Km)"}, 0);
        tableAlertas = new JTable(modelAlertas);
        estilizarTabla(tableAlertas);
        panelAlertas.add(new JScrollPane(tableAlertas), BorderLayout.CENTER);

        // --- BOTONES ---
        JPanel panelOps = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelOps.setBounds(20, 360, 940, 40);
        panelOps.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelOps);

        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLeer = new JButton("Leer");
        panelOps.add(btnAgregar); panelOps.add(btnActualizar); panelOps.add(btnEliminar); panelOps.add(btnLeer);

        // --- TABLA PRINCIPAL ---
        modelPrincipal = new DefaultTableModel(
                new String[]{"ID", "Marca", "Modelo", "Año", "KM Actual", "KM Mant", "Fecha Mant", "ID Cond."}, 0);
        tablePrincipal = new JTable(modelPrincipal);
        estilizarTabla(tablePrincipal);

        JScrollPane scrollPrincipal = new JScrollPane(tablePrincipal);
        scrollPrincipal.setBounds(20, 410, 945, 230);
        panelPrincipal.add(scrollPrincipal);
    }

    private void estilizarTabla(JTable t) {
        t.setRowHeight(30);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JTableHeader header = t.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void cargarCamiones() {
        controlador.cargarCamiones(modelPrincipal);
        actualizarAlertas(); 
    }

    private void limpiarCampos() {
        txtMarca.setText(""); txtModelo.setText(""); txtAnio.setText("");
        txtKmActual.setText(""); txtKmMantenimiento.setText(""); 
        txtFechaMant.setText("DD-MM-AAAA"); txtConductorId.setText("");
    }

    private void actualizarAlertas() {
    modelAlertas.setRowCount(0); 

    // Se recorre todas las filas de la tabla 
    for (int i = 0; i < modelPrincipal.getRowCount(); i++) {
        try {
            int id = Integer.parseInt(modelPrincipal.getValueAt(i, 0).toString());
            String marca = modelPrincipal.getValueAt(i, 1).toString();
            String modelo = modelPrincipal.getValueAt(i, 2).toString();
            double kmActual = Double.parseDouble(modelPrincipal.getValueAt(i, 4).toString());
            double kmUltimoMant = Double.parseDouble(modelPrincipal.getValueAt(i, 5).toString());

            // Calculamos la diferencia
            double kmsRecorridos = kmActual - kmUltimoMant;

            // Si ha recorrido 5.000 km o más, sale la alerta
            if (kmsRecorridos >= 5000) {
                // Agregamos a la tabla de alertas
                modelAlertas.addRow(new Object[]{
                    id, 
                    marca, 
                    modelo, 
                    String.format("%.1f km", kmsRecorridos) // Mostramos el total recorrido
                });
            }
        } catch (Exception e) {
            System.err.println("Error al procesar alerta en fila " + i + ": " + e.getMessage());
        }
        }
    }

    private void agregarListeners() {
        btnAgregar.addActionListener(e -> {
            try {
                controlador.agregarCamion(
                    txtMarca.getText(), 
                    txtModelo.getText(), 
                    txtAnio.getText(), 
                    txtKmActual.getText(), 
                    txtKmMantenimiento.getText(), 
                    txtFechaMant.getText(),
                    Integer.parseInt(txtConductorId.getText()), 
                    modelPrincipal
                );
                limpiarCampos();
                actualizarAlertas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos.");
            }
        });

        btnLeer.addActionListener(e -> {
            int fila = tablePrincipal.getSelectedRow(); 
            if (fila != -1) {
                txtMarca.setText(modelPrincipal.getValueAt(fila, 1).toString());
                txtModelo.setText(modelPrincipal.getValueAt(fila, 2).toString());
                txtAnio.setText(modelPrincipal.getValueAt(fila, 3).toString());
                txtKmActual.setText(modelPrincipal.getValueAt(fila, 4).toString());
                txtKmMantenimiento.setText(modelPrincipal.getValueAt(fila, 5).toString());
                txtFechaMant.setText(modelPrincipal.getValueAt(fila, 6).toString());
                txtConductorId.setText(modelPrincipal.getValueAt(fila, 7).toString());
            } else {
                cargarCamiones();
                limpiarCampos();
            }
        });

        btnActualizar.addActionListener(e -> {
            int fila = tablePrincipal.getSelectedRow();
            if (fila != -1) {
                try {
                    int id = Integer.parseInt(modelPrincipal.getValueAt(fila, 0).toString());
                    controlador.modificarCamion(
                        id, 
                        txtMarca.getText(), 
                        txtModelo.getText(), 
                        txtAnio.getText(), 
                        txtKmActual.getText(), 
                        txtKmMantenimiento.getText(), 
                        txtFechaMant.getText(),
                        Integer.parseInt(txtConductorId.getText()), 
                        modelPrincipal
                    );
                    limpiarCampos();
                    actualizarAlertas();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al actualizar.");
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            int f = tablePrincipal.getSelectedRow();
            if (f != -1) {
                controlador.eliminarCamion(f, modelPrincipal);
            }
        });
    }
}