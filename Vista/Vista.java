package Vista;

import Controlador.ControladorCamion;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class Vista extends JFrame {

    private JTextField txtMarca, txtModelo, txtAnio, txtKmActual, txtKmMantenimiento, txtConductorId;
    private JTextArea areaAlertas; // Nuevo para las alertas
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLeer;
    private ControladorCamion controlador;
    private JTable table;
    private DefaultTableModel tableModel;

    public Vista() {
        configuracion();
        agregarListeners();
        cargarCamiones();
        this.setVisible(true);
    }

    private void configuracion() {
        controlador = new ControladorCamion();
        setTitle("Gestión de Flota - Camiones");
        setSize(900, 650); // Ampliado para que quepa el panel lateral
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(null);
        panelPrincipal.setBounds(0, 0, 880, 610);
        panelPrincipal.setBackground(Color.decode("#FFFFE0"));
        add(panelPrincipal);

        // --- TU PANEL DE DATOS (Ajustado en ancho para hacer espacio) ---
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(null);
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Camión"));
        panelDatos.setBounds(10, 60, 420, 280); // Ancho reducido de 600 a 420
        panelDatos.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelDatos);

        // Tus etiquetas y campos originales
        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setBounds(20, 30, 80, 25);
        panelDatos.add(lblMarca);
        txtMarca = new JTextField();
        txtMarca.setBounds(130, 30, 200, 25);
        panelDatos.add(txtMarca);

        JLabel lblModelo = new JLabel("Modelo:");
        lblModelo.setBounds(20, 70, 80, 25);
        panelDatos.add(lblModelo);
        txtModelo = new JTextField();
        txtModelo.setBounds(130, 70, 200, 25);
        panelDatos.add(txtModelo);

        JLabel lblAnio = new JLabel("Año:");
        lblAnio.setBounds(20, 110, 80, 25);
        panelDatos.add(lblAnio);
        txtAnio = new JTextField();
        txtAnio.setBounds(130, 110, 200, 25);
        panelDatos.add(txtAnio);

        JLabel lblConductor = new JLabel("ID Cond.:");
        lblConductor.setBounds(20, 150, 80, 25);
        panelDatos.add(lblConductor);
        txtConductorId = new JTextField();
        txtConductorId.setBounds(130, 150, 200, 25);
        panelDatos.add(txtConductorId);

        JLabel lblKm = new JLabel("KM Actual:");
        lblKm.setBounds(20, 190, 80, 25);
        panelDatos.add(lblKm);
        txtKmActual = new JTextField();
        txtKmActual.setBounds(130, 190, 200, 25);
        panelDatos.add(txtKmActual);

        JLabel lblKmMant = new JLabel("Último Mant.:");
        lblKmMant.setBounds(20, 230, 100, 25);
        panelDatos.add(lblKmMant);
        txtKmMantenimiento = new JTextField();
        txtKmMantenimiento.setBounds(130, 230, 200, 25);
        panelDatos.add(txtKmMantenimiento);

        // --- NUEVO: PANEL DE ALERTAS (A la derecha) ---
        JPanel panelAlertas = new JPanel();
        panelAlertas.setLayout(null);
        panelAlertas.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), 
                "[ PANEL DE ALERTAS MANTENIMIENTO ]", TitledBorder.LEFT, TitledBorder.TOP));
        panelAlertas.setBounds(440, 60, 420, 280);
        panelAlertas.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelAlertas);

        areaAlertas = new JTextArea();
        areaAlertas.setEditable(false);
        areaAlertas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaAlertas.setBackground(Color.decode("#FFFFE0"));
        
        JScrollPane scrollAlertas = new JScrollPane(areaAlertas);
        scrollAlertas.setBounds(15, 30, 390, 230);
        scrollAlertas.setBorder(null);
        panelAlertas.add(scrollAlertas);

        // --- TUS BOTONES ---
        JPanel panelOps = new JPanel();
        panelOps.setLayout(new FlowLayout());
        panelOps.setBounds(240, 370, 400, 60);
        panelOps.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelOps);

        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLeer = new JButton("Refrescar");
        panelOps.add(btnAgregar); panelOps.add(btnActualizar); panelOps.add(btnEliminar); panelOps.add(btnLeer);

        // --- TU TABLA ---
        tableModel = new DefaultTableModel(
                new String[] { "ID", "Marca", "Modelo", "Año", "KM Actual", "Últ. Mant", "ID Cond." }, 0);
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 440, 850, 150);
        panelPrincipal.add(scroll);
    }

    // NUEVO MÉTODO: Analiza la tabla y muestra quién necesita mantenimiento
    private void actualizarAlertasAutomaticas() {
        StringBuilder sb = new StringBuilder();
        sb.append("⚠️ ¡ATENCIÓN!\n");
        sb.append("Camiones que superan los 5000 km:\n");
        sb.append("----------------------------------\n");
        boolean hayAlertas = false;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                String marca = tableModel.getValueAt(i, 1).toString();
                String modelo = tableModel.getValueAt(i, 2).toString();
                double kmAct = Double.parseDouble(tableModel.getValueAt(i, 4).toString());
                double kmMant = Double.parseDouble(tableModel.getValueAt(i, 5).toString());

                double diferencia = kmAct - kmMant;

                if (diferencia >= 5000) {
                    sb.append("- ").append(marca).append(" ").append(modelo)
                      .append("\n  Exceso: ").append(diferencia - 5000).append(" km\n\n");
                    hayAlertas = true;
                }
            } catch (Exception e) { }
        }

        if (!hayAlertas) {
            areaAlertas.setText("✅ Todos los camiones están\ndentro del rango de kilometraje.");
            areaAlertas.setForeground(new Color(0, 128, 0));
        } else {
            areaAlertas.setText(sb.toString());
            areaAlertas.setForeground(Color.RED);
        }
    }

    private void limpiarCampos() {
        txtMarca.setText(""); txtModelo.setText(""); txtAnio.setText("");
        txtKmActual.setText(""); txtKmMantenimiento.setText(""); txtConductorId.setText("");
    }

    private void cargarCamiones() {
        try {
            controlador.cargarCamiones(tableModel);
            actualizarAlertasAutomaticas(); // Se actualiza cada vez que cargan datos
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void agregarListeners() {
        btnAgregar.addActionListener(e -> {
            try {
                controlador.agregarCamion(txtMarca.getText(), txtModelo.getText(), txtAnio.getText(), 
                        txtKmActual.getText(), txtKmMantenimiento.getText(), 
                        Integer.parseInt(txtConductorId.getText()), tableModel);
                limpiarCampos();
                cargarCamiones();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El ID de conductor debe ser un número.");
            }
        });

        btnLeer.addActionListener(e -> cargarCamiones());

        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila != -1) {
                controlador.eliminarCamion(fila, tableModel);
                cargarCamiones();
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila != -1) {
                txtMarca.setText(tableModel.getValueAt(fila, 1).toString());
                txtModelo.setText(tableModel.getValueAt(fila, 2).toString());
                txtAnio.setText(tableModel.getValueAt(fila, 3).toString());
                txtKmActual.setText(tableModel.getValueAt(fila, 4).toString());
                txtKmMantenimiento.setText(tableModel.getValueAt(fila, 5).toString());
                txtConductorId.setText(tableModel.getValueAt(fila, 6).toString());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Vista());
    }
}