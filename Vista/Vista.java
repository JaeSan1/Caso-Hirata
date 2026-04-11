package Vista;

import Controlador.ControladorCamion;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Vista extends JFrame {

    private JTextField txtMarca, txtModelo, txtAnio, txtKmActual, txtKmMantenimiento, txtConductorId;
    private JTable tableAlertas; 
    private DefaultTableModel modelAlertas;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLeer;
    private ControladorCamion controlador;
    private JTable tablePrincipal;
    private DefaultTableModel modelPrincipal;

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
        setSize(1000, 700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(null);
        panelPrincipal.setBounds(0, 0, 985, 660);
        panelPrincipal.setBackground(Color.decode("#FFFFE0"));
        add(panelPrincipal);

        
        JPanel panelDatos = new JPanel(null);
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Camión"));
        panelDatos.setBounds(20, 40, 440, 280);
        panelDatos.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelDatos);

        int y = 30, gap = 35;
        JLabel lblMarca = new JLabel("Marca:"); lblMarca.setBounds(25, y, 100, 25);
        txtMarca = new JTextField(); txtMarca.setBounds(130, y, 280, 25);
        panelDatos.add(lblMarca); panelDatos.add(txtMarca);

        JLabel lblModelo = new JLabel("Modelo:"); lblModelo.setBounds(25, y + gap, 100, 25);
        txtModelo = new JTextField(); txtModelo.setBounds(130, y + gap, 280, 25);
        panelDatos.add(lblModelo); panelDatos.add(txtModelo);

        JLabel lblAnio = new JLabel("Año:"); lblAnio.setBounds(25, y + (gap * 2), 100, 25);
        txtAnio = new JTextField(); txtAnio.setBounds(130, y + (gap * 2), 280, 25);
        panelDatos.add(lblAnio); panelDatos.add(txtAnio);

        JLabel lblCond = new JLabel("ID Cond.:"); lblCond.setBounds(25, y + (gap * 3), 100, 25);
        txtConductorId = new JTextField(); txtConductorId.setBounds(130, y + (gap * 3), 280, 25);
        panelDatos.add(lblCond); panelDatos.add(txtConductorId);

        JLabel lblKmAct = new JLabel("KM Actual:"); lblKmAct.setBounds(25, y + (gap * 4), 100, 25);
        txtKmActual = new JTextField(); txtKmActual.setBounds(130, y + (gap * 4), 280, 25);
        panelDatos.add(lblKmAct); panelDatos.add(txtKmActual);

        JLabel lblKmMant = new JLabel("Último Mant.:"); lblKmMant.setBounds(25, y + (gap * 5), 100, 25);
        txtKmMantenimiento = new JTextField(); txtKmMantenimiento.setBounds(130, y + (gap * 5), 280, 25);
        panelDatos.add(lblKmMant); panelDatos.add(txtKmMantenimiento);

        
        JPanel panelAlertas = new JPanel(new BorderLayout());
        panelAlertas.setBorder(BorderFactory.createTitledBorder("[ PANEL DE ALERTAS MANTENIMIENTO ]"));
        panelAlertas.setBounds(480, 40, 480, 280);
        panelAlertas.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelAlertas);

        modelAlertas = new DefaultTableModel(new String[]{"ID", "Marca", "Modelo", "Exceso (Km)"}, 0);
        tableAlertas = new JTable(modelAlertas);
        estilizarTabla(tableAlertas);
        
        JScrollPane scrollAlertas = new JScrollPane(tableAlertas);
        scrollAlertas.getViewport().setBackground(Color.WHITE);
        panelAlertas.add(scrollAlertas, BorderLayout.CENTER);

        
        JPanel panelOps = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelOps.setBounds(20, 335, 940, 40);
        panelOps.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelOps);

        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLeer = new JButton("Leer");
        panelOps.add(btnAgregar); panelOps.add(btnActualizar); panelOps.add(btnEliminar); panelOps.add(btnLeer);

       
        modelPrincipal = new DefaultTableModel(
                new String[]{"ID", "Marca", "Modelo", "Año", "KM Actual", "Últ. Mant", "ID Cond."}, 0);
        tablePrincipal = new JTable(modelPrincipal);
        estilizarTabla(tablePrincipal);

        JScrollPane scrollPrincipal = new JScrollPane(tablePrincipal);
        scrollPrincipal.setBounds(20, 385, 945, 250);
        scrollPrincipal.getViewport().setBackground(Color.WHITE);
        panelPrincipal.add(scrollPrincipal);
    }

    private void estilizarTabla(JTable t) {
        t.setRowHeight(30);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JTableHeader header = t.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 35));
        t.setGridColor(Color.decode("#D3D3D3"));
        t.setSelectionBackground(Color.decode("#CCE5FF"));
        t.setShowGrid(true);
    }

    
    private void actualizarAlertas() {
        modelAlertas.setRowCount(0); 
        
        for (int i = 0; i < modelPrincipal.getRowCount(); i++) {
            try {
                int id = Integer.parseInt(modelPrincipal.getValueAt(i, 0).toString());
                String marca = modelPrincipal.getValueAt(i, 1).toString();
                String modelo = modelPrincipal.getValueAt(i, 2).toString();
                double kmAct = Double.parseDouble(modelPrincipal.getValueAt(i, 4).toString());
                double kmMant = Double.parseDouble(modelPrincipal.getValueAt(i, 5).toString());

                double diferencia = kmAct - kmMant;

              
                if (diferencia >= 5000) {
                    modelAlertas.addRow(new Object[]{
                        id,
                        marca,
                        modelo,
                        String.format("%.1f km", diferencia - 5000)
                    });
                }
            } catch (Exception e) {
                
            }
        }
    }

    private void cargarCamiones() {
        try {
            controlador.cargarCamiones(modelPrincipal);
            actualizarAlertas(); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión con la base de datos.");
        }
    }

    private void limpiarCampos() {
        txtMarca.setText(""); txtModelo.setText(""); txtAnio.setText("");
        txtKmActual.setText(""); txtKmMantenimiento.setText(""); txtConductorId.setText("");
    }

    private void agregarListeners() {
        btnAgregar.addActionListener(e -> {
            try {
                controlador.agregarCamion(txtMarca.getText(), txtModelo.getText(), txtAnio.getText(), 
                        txtKmActual.getText(), txtKmMantenimiento.getText(), 
                        Integer.parseInt(txtConductorId.getText()), modelPrincipal);
                limpiarCampos();
                cargarCamiones(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Verifique que los campos numéricos sean correctos.");
            }
        });


    // FUNCIÓN DEL BOTÓN LEER 
    btnLeer.addActionListener(e -> {
        int fila = tablePrincipal.getSelectedRow(); 
        
        if (fila != -1) {
        
            txtMarca.setText(modelPrincipal.getValueAt(fila, 1).toString());
            txtModelo.setText(modelPrincipal.getValueAt(fila, 2).toString());
            txtAnio.setText(modelPrincipal.getValueAt(fila, 3).toString());
            txtKmActual.setText(modelPrincipal.getValueAt(fila, 4).toString());
            txtKmMantenimiento.setText(modelPrincipal.getValueAt(fila, 5).toString());
            txtConductorId.setText(modelPrincipal.getValueAt(fila, 6).toString());
            
         
            System.out.println("Datos cargados desde la tabla.");
        } else {
          
            cargarCamiones();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Seleccione una fila para leer sus datos o presione de nuevo para refrescar la lista.");
        }
    });

        btnEliminar.addActionListener(e -> {
            int f = tablePrincipal.getSelectedRow();
            if (f != -1) {
                controlador.eliminarCamion(f, modelPrincipal);
                cargarCamiones();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un camión de la tabla inferior.");
            }
        });
        // FUNCIÓN DEL BOTÓN ACTUALIZAR
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
                    Integer.parseInt(txtConductorId.getText()), 
                    modelPrincipal                      
                );

                limpiarCampos();
                cargarCamiones();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Datos numéricos incorrectos.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una fila de la tabla primero.");
        }
    });
        
    }
}