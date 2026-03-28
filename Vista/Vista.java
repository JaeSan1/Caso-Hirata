package Vista;

import Controlador.ControladorCamion;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class Vista extends JFrame {

    private JTextField txtMarca, txtModelo, txtAnio, txtKmActual, txtKmMantenimiento, txtConductorId;
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
        setSize(650, 650);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(null);
        panelPrincipal.setBounds(0, 0, 630, 610);
        panelPrincipal.setBackground(Color.decode("#FFFFE0"));
        add(panelPrincipal);

        // --- Panel de Datos (Ajustado a los campos de Camion) ---
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(null);
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Camión"));
        panelDatos.setBounds(10, 10, 600, 180);
        panelDatos.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelDatos);

        // Fila 1
        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setBounds(20, 30, 80, 25);
        panelDatos.add(lblMarca);
        txtMarca = new JTextField();
        txtMarca.setBounds(100, 30, 150, 25);
        panelDatos.add(txtMarca);

        JLabel lblModelo = new JLabel("Modelo:");
        lblModelo.setBounds(300, 30, 80, 25);
        panelDatos.add(lblModelo);
        txtModelo = new JTextField();
        txtModelo.setBounds(400, 30, 150, 25);
        panelDatos.add(txtModelo);

        // Fila 2
        JLabel lblAnio = new JLabel("Año:");
        lblAnio.setBounds(20, 70, 80, 25);
        panelDatos.add(lblAnio);
        txtAnio = new JTextField();
        txtAnio.setBounds(100, 70, 150, 25);
        panelDatos.add(txtAnio);

        JLabel lblConductor = new JLabel("ID Cond.:");
        lblConductor.setBounds(300, 70, 80, 25);
        panelDatos.add(lblConductor);
        txtConductorId = new JTextField();
        txtConductorId.setBounds(400, 70, 150, 25);
        panelDatos.add(txtConductorId);

        // Fila 3
        JLabel lblKm = new JLabel("KM Actual:");
        lblKm.setBounds(20, 110, 80, 25);
        panelDatos.add(lblKm);
        txtKmActual = new JTextField();
        txtKmActual.setBounds(100, 110, 150, 25);
        panelDatos.add(txtKmActual);

        JLabel lblKmMant = new JLabel("Último Mant.:");
        lblKmMant.setBounds(300, 110, 100, 25);
        panelDatos.add(lblKmMant);
        txtKmMantenimiento = new JTextField();
        txtKmMantenimiento.setBounds(400, 110, 150, 25);
        panelDatos.add(txtKmMantenimiento);

        // --- Botones Operaciones ---
        JPanel panelOps = new JPanel();
        panelOps.setLayout(new FlowLayout());
        panelOps.setBounds(10, 200, 600, 50);
        panelOps.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelOps);

        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLeer = new JButton("Refrescar");
        panelOps.add(btnAgregar); panelOps.add(btnActualizar); panelOps.add(btnEliminar); panelOps.add(btnLeer);

        // --- Tabla ---
        tableModel = new DefaultTableModel(
                new String[] { "ID", "Marca", "Modelo", "Año", "KM Actual", "Últ. Mant", "ID Cond." }, 0);
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 260, 600, 300);
        panelPrincipal.add(scroll);
    }

    private void limpiarCampos() {
        txtMarca.setText(""); txtModelo.setText(""); txtAnio.setText("");
        txtKmActual.setText(""); txtKmMantenimiento.setText(""); txtConductorId.setText("");
    }

    private void cargarCamiones() {
        try {
            controlador.cargarCamiones(tableModel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void agregarListeners() {
        btnAgregar.addActionListener(e -> {
            try {
                // Aquí adaptamos los datos para enviarlos al controlador
                // Nota: Asegúrate de que tu controlador reciba estos campos específicos
                String marca = txtMarca.getText();
                String modelo = txtModelo.getText();
                int anio = Integer.parseInt(txtAnio.getText());
                double kmA = Double.parseDouble(txtKmActual.getText());
                double kmM = Double.parseDouble(txtKmMantenimiento.getText());
                int condId = Integer.parseInt(txtConductorId.getText());

                // Llamada al controlador (asumiendo que actualizaste su firma)
                // Si tu controlador aún pide patente/ubicación, tendrás que cambiarlo también.
                controlador.agregarCamion(marca, modelo, anio, kmA, kmM, condId, tableModel);
                limpiarCampos();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese números válidos en Año, KM e ID.");
            }
            cargarCamiones();
        });

        btnLeer.addActionListener(e -> cargarCamiones());

        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila != -1) {
                controlador.eliminarCamion(fila, tableModel);
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