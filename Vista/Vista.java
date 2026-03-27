package Vista;

import Controlador.ControladorCamion;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class Vista extends JFrame {

    private TextField txtPatente, txtConductor, txtUbicacion, txtFechaMantenimiento;
    private Checkbox chkActivo;
    private Button btnAgregar, btnActualizar, btnEliminar, btnLeer;
    private ControladorCamion controlador;
    private JTable table;
    private DefaultTableModel tableModel;

public class Ventana extends JFrame {
    public Ventana() {
        setTitle("My Application");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add components here if needed, e.g., panels, buttons
    }
}

    private void configuracion() {
        controlador = new ControladorCamion();
        setTitle("Gestión de Flota 4.0");
        setSize(600, 600);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(null);
        panelPrincipal.setBounds(20, 20, 550, 550);
        panelPrincipal.setBackground(Color.decode("#FFFFE0"));
        add(panelPrincipal);


        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(null);
        panelDatos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                "Datos Camión", TitledBorder.LEFT, TitledBorder.TOP));
        panelDatos.setBounds(10, 10, 520, 200);
        panelDatos.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelDatos);

        Label lblPatente = new Label("Patente:");
        lblPatente.setBounds(20, 30, 100, 25);
        panelDatos.add(lblPatente);
        txtPatente = new TextField();
        txtPatente.setBounds(130, 30, 200, 25);
        panelDatos.add(txtPatente);

        Label lblConductor = new Label("Conductor:");
        lblConductor.setBounds(20, 60, 100, 25);
        panelDatos.add(lblConductor);
        txtConductor = new TextField();
        txtConductor.setBounds(130, 60, 200, 25);
        panelDatos.add(txtConductor);

        Label lblUbicacion = new Label("Ubicación GPS:");
        lblUbicacion.setBounds(20, 90, 100, 25);
        panelDatos.add(lblUbicacion);
        txtUbicacion = new TextField();
        txtUbicacion.setBounds(130, 90, 200, 25);
        panelDatos.add(txtUbicacion);

        Label lblFecha = new Label("F. Mantenimiento:");
        lblFecha.setBounds(20, 120, 100, 25);
        panelDatos.add(lblFecha);
        txtFechaMantenimiento = new TextField();
        txtFechaMantenimiento.setBounds(130, 120, 200, 25);
        panelDatos.add(txtFechaMantenimiento);


        chkActivo = new Checkbox("Activo");
        chkActivo.setBounds(350, 30, 100, 25);
        panelDatos.add(chkActivo);


        JPanel panelOperaciones = new JPanel();
        panelOperaciones.setLayout(null);
        panelOperaciones.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                "Operaciones", TitledBorder.LEFT, TitledBorder.TOP));
        panelOperaciones.setBounds(10, 220, 520, 80);
        panelOperaciones.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelOperaciones);

        btnAgregar = new Button("Agregar");
        btnAgregar.setBounds(20, 30, 100, 30);
        panelOperaciones.add(btnAgregar);

        btnActualizar = new Button("Actualizar");
        btnActualizar.setBounds(130, 30, 100, 30);
        panelOperaciones.add(btnActualizar);

        btnEliminar = new Button("Eliminar");
        btnEliminar.setBounds(240, 30, 100, 30);
        panelOperaciones.add(btnEliminar);

        btnLeer = new Button("Leer");
        btnLeer.setBounds(350, 30, 100, 30);
        panelOperaciones.add(btnLeer);

        JPanel panelBaseDatos = new JPanel();
        panelBaseDatos.setLayout(new BorderLayout());
        panelBaseDatos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                "Base de Datos", TitledBorder.LEFT, TitledBorder.TOP));
        panelBaseDatos.setBounds(10, 310, 520, 200);
        panelBaseDatos.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelBaseDatos);


        tableModel = new DefaultTableModel(
                new String[] { "ID", "Patente", "Conductor", "Ubicación", "Estado", "F. Mantenimiento" }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panelBaseDatos.add(scrollPane);
    }
    

    private void limpiarCampos() {
        txtPatente.setText("");
        txtConductor.setText("");
        txtUbicacion.setText("");
        txtFechaMantenimiento.setText("");
        chkActivo.setState(false);
    }


    private void cargarCamiones() {
        try {
            controlador.cargarCamiones(tableModel);
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(
                null, 
                "Error al cargar datos: " + ex.getMessage() + "\nVerifique la conexión a la base de datos.", 
                "Error de Carga", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void agregarListeners() {
        

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String patenteString = txtPatente.getText(); 
                    String conductorString = txtConductor.getText();
                    String ubicacionGpsString = txtUbicacion.getText();
                    String fechamantenimientoString = txtFechaMantenimiento.getText();
                    boolean estado = chkActivo.getState();

                    controlador.agregarCamion(patenteString, conductorString, ubicacionGpsString, fechamantenimientoString, estado, tableModel);
                    
                    cargarCamiones(); 
                    limpiarCampos();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Error de base de datos: " + ex.getMessage() + "\nVerifique que la Patente no esté duplicada.", 
                        "Error de Persistencia", 
                        JOptionPane.ERROR_MESSAGE
                    );
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Error de formato de fecha. Use AAAA-MM-DD (e.g., 2024-07-25).", 
                        "Error de Entrada", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    String patenteString = txtPatente.getText(); 
                    String conductorString = txtConductor.getText();
                    String ubicacionGpsString = txtUbicacion.getText();
                    String fechamantenimientoString = txtFechaMantenimiento.getText();
                    boolean estado = chkActivo.getState();


                    controlador.modificarCamion(selectedRow, patenteString, conductorString, ubicacionGpsString, fechamantenimientoString, estado, tableModel);
                    
                    limpiarCampos();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error de BD al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(null, "Error de formato de fecha. Use AAAA-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    
                    controlador.eliminarCamion(tableModel, selectedRow);
                    limpiarCampos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error de BD al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnLeer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarCamiones();
                limpiarCampos();
            }
        });
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = table.getSelectedRow();
                if (filaSeleccionada != -1) {

                    txtPatente.setText(tableModel.getValueAt(filaSeleccionada, 1).toString());
                    txtConductor.setText(tableModel.getValueAt(filaSeleccionada, 2).toString());
                    txtUbicacion.setText(tableModel.getValueAt(filaSeleccionada, 3).toString());
                    
                    String estadoTexto = tableModel.getValueAt(filaSeleccionada, 4).toString();
                    chkActivo.setState(estadoTexto.equals("Activo"));
                    
                    txtFechaMantenimiento.setText(tableModel.getValueAt(filaSeleccionada, 5).toString());
                }
            }
        });
    }
}