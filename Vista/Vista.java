package Vista;

import Controlador.ControladorCamion;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Vista extends JFrame {

    // Formulario 
    private JTextField txtMarca, txtModelo, txtAnio, txtKmActual, txtKmMantenimiento, txtFechaMant;
    private JTextField txtTipoMant, txtDescripcionMant; 
    private JTextField txtNombreCond, txtLicencia, txtTelefono;
    
    // Tablas y Modelos 
    private JTable tablePrincipal, tableAlertas;
    private DefaultTableModel modelPrincipal, modelAlertas;
    
    // Botones 
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLeer;
    
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
        setSize(1150, 720); 
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(null);
        panelPrincipal.setBounds(0, 0, 1135, 680);
        panelPrincipal.setBackground(Color.decode("#FFFFE0")); 
        add(panelPrincipal);

        //PANEL DE DATOS 
        JPanel panelDatos = new JPanel(null);
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Vehículo y Personal"));
        panelDatos.setBounds(20, 20, 440, 390); // Altura compactada según tu dibujo
        panelDatos.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelDatos);

        int y = 25, gap = 31; 
        int lblW = 110, txtW = 280;

        panelDatos.add(new JLabel("Marca:")).setBounds(25, y, lblW, 25);
        txtMarca = new JTextField(); txtMarca.setBounds(140, y, txtW, 25); panelDatos.add(txtMarca);
        y += gap;
        panelDatos.add(new JLabel("Modelo:")).setBounds(25, y, lblW, 25);
        txtModelo = new JTextField(); txtModelo.setBounds(140, y, txtW, 25); panelDatos.add(txtModelo);
        y += gap;
        panelDatos.add(new JLabel("Año:")).setBounds(25, y, lblW, 25);
        txtAnio = new JTextField(); txtAnio.setBounds(140, y, txtW, 25); panelDatos.add(txtAnio);
        y += gap;
        panelDatos.add(new JLabel("KM Actual:")).setBounds(25, y, lblW, 25);
        txtKmActual = new JTextField(); txtKmActual.setBounds(140, y, txtW, 25); panelDatos.add(txtKmActual);
        y += gap;
        panelDatos.add(new JLabel("KM Últ. Mant.:")).setBounds(25, y, lblW, 25);
        txtKmMantenimiento = new JTextField(); txtKmMantenimiento.setBounds(140, y, txtW, 25); panelDatos.add(txtKmMantenimiento);
        y += gap;
        panelDatos.add(new JLabel("Fecha Mant.:")).setBounds(25, y, lblW, 25);
        txtFechaMant = new JTextField("DD-MM-YYYY"); txtFechaMant.setBounds(140, y, txtW, 25); panelDatos.add(txtFechaMant);
        y += gap;
        panelDatos.add(new JLabel("Tipo Mant.:")).setBounds(25, y, lblW, 25);
        txtTipoMant = new JTextField(); txtTipoMant.setBounds(140, y, txtW, 25); panelDatos.add(txtTipoMant);
        y += gap;
        panelDatos.add(new JLabel("Descripción:")).setBounds(25, y, lblW, 25);
        txtDescripcionMant = new JTextField(); txtDescripcionMant.setBounds(140, y, txtW, 25); panelDatos.add(txtDescripcionMant);
        y += gap;
        panelDatos.add(new JLabel("Nombre Cond.:")).setBounds(25, y, lblW, 25);
        txtNombreCond = new JTextField(); txtNombreCond.setBounds(140, y, txtW, 25); panelDatos.add(txtNombreCond);
        y += gap;
        panelDatos.add(new JLabel("Licencia:")).setBounds(25, y, lblW, 25);
        txtLicencia = new JTextField(); txtLicencia.setBounds(140, y, txtW, 25); panelDatos.add(txtLicencia);
        y += gap;
        panelDatos.add(new JLabel("Teléfono:")).setBounds(25, y, lblW, 25);
        txtTelefono = new JTextField(); txtTelefono.setBounds(140, y, txtW, 25); panelDatos.add(txtTelefono);

        // PANEL DE ALERTAS 
        JPanel panelAlertas = new JPanel(new BorderLayout());
        panelAlertas.setBorder(BorderFactory.createTitledBorder("[ PANEL DE ALERTAS MANTENIMIENTO ]"));
        panelAlertas.setBounds(480, 20, 630, 390);
        panelAlertas.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelAlertas);

        modelAlertas = new DefaultTableModel(new String[]{"Vehículo", "Exceso (Km)", "Contacto Conductor"}, 0);
        tableAlertas = new JTable(modelAlertas);
        estilizarTabla(tableAlertas);
        panelAlertas.add(new JScrollPane(tableAlertas), BorderLayout.CENTER);

        //  PANEL DE BOTONES 
        JPanel panelOps = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelOps.setBounds(20, 420, 1090, 40);
        panelOps.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelOps);

        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLeer = new JButton("Leer");
        panelOps.add(btnAgregar); panelOps.add(btnActualizar); panelOps.add(btnEliminar); panelOps.add(btnLeer);

        // TABLA PRINCIPAL 
        String[] columnas = {"ID", "Marca", "Modelo", "Año", "KM Actual", "KM Mant", "Fecha", "Tipo", "Descripción", "Conductor", "Licencia", "Teléfono"};
        modelPrincipal = new DefaultTableModel(columnas, 0);
        tablePrincipal = new JTable(modelPrincipal);
        
        // Se oculta el ID pero se mantiene en el sistema
        tablePrincipal.getColumnModel().getColumn(0).setMinWidth(0);
        tablePrincipal.getColumnModel().getColumn(0).setMaxWidth(0);
        tablePrincipal.getColumnModel().getColumn(0).setWidth(0);
        
        estilizarTabla(tablePrincipal);

        JScrollPane scrollPrincipal = new JScrollPane(tablePrincipal);
        scrollPrincipal.setBounds(20, 470, 1090, 180); 
        panelPrincipal.add(scrollPrincipal);

        // Encadenamos los campos para que el Enter nos envie al siguiente
        configurarEnter(txtMarca, txtModelo);
        configurarEnter(txtModelo, txtAnio);
        configurarEnter(txtAnio, txtKmActual);
        configurarEnter(txtKmActual, txtKmMantenimiento);
        configurarEnter(txtKmMantenimiento, txtFechaMant);
        configurarEnter(txtFechaMant, txtTipoMant);
        configurarEnter(txtTipoMant, txtDescripcionMant);
        configurarEnter(txtDescripcionMant, txtNombreCond);
        configurarEnter(txtNombreCond, txtLicencia);
        configurarEnter(txtLicencia, txtTelefono);
        configurarEnter(txtTelefono, null); // El último campo ejecuta el botón Agregar
    }

    private void estilizarTabla(JTable t) {
        t.setRowHeight(25);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JTableHeader header = t.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void agregarListeners() {
        btnAgregar.addActionListener(e -> {
            controlador.agregarCamion(
                txtMarca.getText(), txtModelo.getText(), txtAnio.getText(), 
                txtKmActual.getText(), txtKmMantenimiento.getText(), txtFechaMant.getText(),
                txtTipoMant.getText(), txtDescripcionMant.getText(),
                txtNombreCond.getText(), txtLicencia.getText(), txtTelefono.getText(),
                modelPrincipal
            );
            limpiarCampos();
            actualizarAlertas();
        });

        btnLeer.addActionListener(e -> {
            int fila = tablePrincipal.getSelectedRow(); 
            if (fila != -1) {
                txtMarca.setText(getVal(fila, 1));
                txtModelo.setText(getVal(fila, 2));
                txtAnio.setText(getVal(fila, 3));
                txtKmActual.setText(getVal(fila, 4));
                txtKmMantenimiento.setText(getVal(fila, 5));
                txtFechaMant.setText(getVal(fila, 6));
                txtTipoMant.setText(getVal(fila, 7));
                txtDescripcionMant.setText(getVal(fila, 8));
                txtNombreCond.setText(getVal(fila, 9));
                txtLicencia.setText(getVal(fila, 10));
                txtTelefono.setText(getVal(fila, 11));
            }
        });

        btnActualizar.addActionListener(e -> {
            int fila = tablePrincipal.getSelectedRow();
            if (fila != -1) {
                int id = Integer.parseInt(modelPrincipal.getValueAt(fila, 0).toString());
                controlador.modificarCamion(
                    id, txtMarca.getText(), txtModelo.getText(), txtAnio.getText(), 
                    txtKmActual.getText(), txtKmMantenimiento.getText(), txtFechaMant.getText(),
                    txtTipoMant.getText(), txtDescripcionMant.getText(),
                    txtNombreCond.getText(), txtLicencia.getText(), txtTelefono.getText(),
                    modelPrincipal
                );
                limpiarCampos();
                actualizarAlertas();
            }
        });

        btnEliminar.addActionListener(e -> {
            int f = tablePrincipal.getSelectedRow();
            if (f != -1) {
                controlador.eliminarCamion(f, modelPrincipal);
                actualizarAlertas();
            }
        });
    }

    private String getVal(int f, int c) {
        Object obj = modelPrincipal.getValueAt(f, c);
        return obj != null ? obj.toString() : "";
    }

    private void cargarCamiones() {
        controlador.cargarCamiones(modelPrincipal);
        actualizarAlertas(); 
    }

    private void actualizarAlertas() {
        modelAlertas.setRowCount(0); 
        for (int i = 0; i < modelPrincipal.getRowCount(); i++) {
            try {
                double kmActual = Double.parseDouble(modelPrincipal.getValueAt(i, 4).toString());
                double kmUltimoMant = Double.parseDouble(modelPrincipal.getValueAt(i, 5).toString());
                double dif = kmActual - kmUltimoMant;

                if (dif >= 5000) {
                    modelAlertas.addRow(new Object[]{
                        modelPrincipal.getValueAt(i, 1) + " " + modelPrincipal.getValueAt(i, 2), 
                        String.format("%.1f km", dif),
                        modelPrincipal.getValueAt(i, 9),
                        modelPrincipal.getValueAt(i, 11)
                    });
                }
            } catch (Exception e) {}
        }
    }

    private void limpiarCampos() {
        txtMarca.setText(""); txtModelo.setText(""); txtAnio.setText("");
        txtKmActual.setText(""); txtKmMantenimiento.setText(""); 
        txtFechaMant.setText("DD-MM-YYYY"); txtTipoMant.setText("");
        txtDescripcionMant.setText(""); txtNombreCond.setText("");
        txtLicencia.setText(""); txtTelefono.setText("");
    }

    private void configurarEnter(JTextField actual, JTextField siguiente) {
    actual.addActionListener(e -> {
        if (siguiente != null) {
            siguiente.requestFocus(); // Nos mueve al siguiente campo
        } else {
            btnAgregar.doClick(); // Si es el último campo, simula el click en Agregar
        }
    });
}
}