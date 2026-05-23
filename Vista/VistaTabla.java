package Vista;

import Controlador.ControladorFlota;
import Controlador.ControladorOficina;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class VistaTabla extends JFrame {
    // Componentes de interfaz
    private List<JTextField> campos = new ArrayList<>();
    private JTable tablePrincipal, tableAlertas;
    private DefaultTableModel modelPrincipal, modelAlertas;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLeer;
    
    // Atributo global para la barra de búsqueda
    private JTextField txtBuscarGlobal;
    
    // Controladores
    private String tituloModulo;
    private final ControladorFlota ctrlFlota = new ControladorFlota();
    private final ControladorOficina ctrlOficina = new ControladorOficina();

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

        //PANEL DE DATOS 
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

        // -BÚSQUEDA 
        JLabel lblBuscar = new JLabel("Buscar en el historial:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtBuscarGlobal = new JTextField();
        txtBuscarGlobal.setToolTipText("Escriba aquí para filtrar en tiempo real");

        if (esCamion) {
            // Barra de búsqueda encima del Monitor de Alertas
            lblBuscar.setBounds(480, 20, 150, 25);
            txtBuscarGlobal.setBounds(640, 20, 470, 25);


            JPanel panelAlertasBox = new JPanel(new BorderLayout());
            panelAlertasBox.setBorder(BorderFactory.createTitledBorder("[ MONITOR DE ALERTAS MANTENIMIENTO ]"));
            panelAlertasBox.setBackground(Color.decode("#FFFFE0"));
            panelAlertasBox.setBounds(480, 145, 630, 260); 
            
            modelAlertas = new DefaultTableModel(new String[]{"Vehículo", "Exceso (Km)", "Estado"}, 0);
            tableAlertas = new JTable(modelAlertas);
            estilizarTabla(tableAlertas);
            panelAlertasBox.add(new JScrollPane(tableAlertas), BorderLayout.CENTER);
            panelPrincipal.add(panelAlertasBox);
        } else {

            // Si NO es camión, usamos el gran espacio vacío de la derecha 
            JPanel panelBusquedaBox = new JPanel(null);
            panelBusquedaBox.setBorder(BorderFactory.createTitledBorder(" Búsqueda Avanzada"));
            panelBusquedaBox.setBackground(Color.decode("#FFFFE0"));
            panelBusquedaBox.setBounds(480, 20, 630, 100); 

            lblBuscar.setBounds(20, 45, 150, 25);
            txtBuscarGlobal.setBounds(175, 45, 430, 25);
            
            panelBusquedaBox.add(lblBuscar);
            panelBusquedaBox.add(txtBuscarGlobal);
            panelPrincipal.add(panelBusquedaBox);
        }

        // Añadimos los elementos de búsqueda al panel principal solo si es el módulo de camiones
        if (esCamion) {
            panelPrincipal.add(lblBuscar);
            panelPrincipal.add(txtBuscarGlobal);
        }

        // PANEL DE BOTONES 
        JPanel panelOps = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelOps.setBounds(20, 425, 1090, 35);
        panelOps.setBackground(Color.decode("#FFFFE0"));
        panelPrincipal.add(panelOps);

        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLeer = new JButton("Leer");
        
        panelOps.add(btnAgregar); panelOps.add(btnActualizar); panelOps.add(btnEliminar); panelOps.add(btnLeer);

        // TABLA PRINCIPAL
        modelPrincipal = new DefaultTableModel(columnas, 0);
        tablePrincipal = new JTable(modelPrincipal);
        estilizarTabla(tablePrincipal);

        // Ocultar ID técnico
        if (tablePrincipal.getColumnCount() > 0) {
            tablePrincipal.getColumnModel().getColumn(0).setMinWidth(0);
            tablePrincipal.getColumnModel().getColumn(0).setMaxWidth(0);
            tablePrincipal.getColumnModel().getColumn(0).setWidth(0);
        }

        JScrollPane scrollPrincipal = new JScrollPane(tablePrincipal);
        scrollPrincipal.setBounds(20, 475, 1090, 180); 
        panelPrincipal.add(scrollPrincipal);

        configurarListeners();
        cargarDatosDesdeBase();
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
            } else if (tituloModulo.contains("Inventario de Equipos")) {
                ctrlOficina.cargarEquipos(modelPrincipal);
            } else if (tituloModulo.contains("Gestión de Software")) {
                ctrlOficina.cargarSoftware(modelPrincipal);
            } else if (tituloModulo.contains("Inventario de Piezas")) {
                ctrlOficina.cargarPiezas(modelPrincipal);
            } else if (tituloModulo.contains("Mantenimiento de Oficina")) {
                ctrlOficina.cargarMantenimientos(modelPrincipal);
            }

            modelPrincipal.fireTableDataChanged();
            tablePrincipal.revalidate();
            tablePrincipal.repaint();
        } catch (Exception e) {
            System.err.println("Error al cargar en la vista: " + e.getMessage());
        }
    }

    private void estilizarTabla(JTable t) {
        t.setRowHeight(25);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JTableHeader header = t.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void configurarListeners() {

        // 1. LEER

        btnLeer.addActionListener(e -> {
            int fila = tablePrincipal.getSelectedRow();
            if (fila >= 0) {
                try {
                    int filaCorrecta = tablePrincipal.convertRowIndexToModel(fila);
                    
                    // Recorremos las cajas de texto del formulario
                    for (int i = 0; i < campos.size(); i++) {
                        if ((i + 1) < modelPrincipal.getColumnCount()) {
                            Object valor = modelPrincipal.getValueAt(filaCorrecta, i + 1);
                            
                            // Validamos que la celda no sea nula para evitar el error 
                            if (valor != null) {
                                campos.get(i).setText(valor.toString());
                            } else {
                                campos.get(i).setText(""); 
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Error controlado al leer fila: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this, "Error al mapear los datos de la fila: " + ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un registro en la tabla para leer.");
            }
        });

        // LÓGICA RF-08: BARRA DE BÚSQUEDA DINÁMICA CORREGIDA (EVITA QUE SE ROMPA LA TABLA)
        TableRowSorter<DefaultTableModel> sorterGlobal = new TableRowSorter<>(modelPrincipal);
        tablePrincipal.setRowSorter(sorterGlobal);

        txtBuscarGlobal.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filtrarHistorial() {
                String texto = txtBuscarGlobal.getText().trim();
                if (texto.isEmpty()) {
                    sorterGlobal.setRowFilter(null); 
                } else {
                    try {
                        // 1. Averiguamos cuántas columnas reales tiene la tabla actual
                        int totalColumnas = modelPrincipal.getColumnCount();
                        
                        // 2. Creamos un arreglo con los índices visibles (desde la 1 hasta la última)
                        // Esto ignora automáticamente la columna 0 (ID oculta)
                        int[] columnasVisibles = new int[totalColumnas - 1];
                        for (int i = 0; i < columnasVisibles.length; i++) {
                            columnasVisibles[i] = i + 1;
                        }
                        
                        // 3. Aplicamos el filtro usando el arreglo dinámico
                        sorterGlobal.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(texto), columnasVisibles));
                    } catch (Exception pse) {
                        System.err.println("Error en patrón de búsqueda: " + pse.getMessage());
                    }
                }
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarHistorial(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarHistorial(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarHistorial(); }
        });

        // 2. ACTUALIZAR
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

                } else if (tituloModulo.contains("Mantenimientos")) {
                    // Capturamos el ID del mantenimiento seleccionado
                    int idMant = Integer.parseInt(modelPrincipal.getValueAt(fila, 0).toString());
                    
                    // Los campos del formulario a la izquierda son 4:
                    String camionIdStr = campos.get(0).getText().trim(); 
                    String fecha = campos.get(1).getText().trim();
                    String tipo = campos.get(2).getText().trim();
                    String desc = campos.get(3).getText().trim();
                    
                    ctrlFlota.actualizarMantenimiento(
                        idMant, 
                        fecha, 
                        tipo, 
                        desc, 
                        "0", 
                        modelPrincipal
                    );  
                
                
                } else if (tituloModulo.contains("Inventario de Equipos")) {
                    ctrlOficina.actualizarEquipo(id, campos.get(0).getText(), campos.get(1).getText(), 
                        campos.get(2).getText(), modelPrincipal);
                } else if (tituloModulo.contains("Gestión de Software")) {
                    ctrlOficina.actualizarSoftware(id, Integer.parseInt(campos.get(0).getText()), campos.get(1).getText(), 
                        campos.get(2).getText(), modelPrincipal);

                } else if (tituloModulo.contains("Inventario de Piezas")) {
                    int idPieza = id; 
                    String nombre = campos.get(0).getText().trim();
                    int stock = Integer.parseInt(campos.get(1).getText().trim());
                    String fecha = campos.get(2).getText().trim();
                    String estado = campos.get(3).getText().trim();
                    
                    ctrlOficina.actualizarPieza(idPieza, nombre, stock, fecha, estado, modelPrincipal);
                    
                } else if (tituloModulo.contains("Mantenimiento de Oficina")) {
                    ctrlOficina.actualizarMantenimiento(
                        id, 
                        Integer.parseInt(campos.get(0).getText()), 
                        campos.get(1).getText(),                  
                        campos.get(2).getText(),                  
                        campos.get(3).getText(),                  
                        modelPrincipal
                    );
                }
                limpiarControles();
            } else {
                JOptionPane.showMessageDialog(this, "Debe 'Leer' un registro primero para actualizarlo.");
            }
        });

        // 3. GUARDAR 
        btnAgregar.addActionListener(e -> {
            try {
                if (tituloModulo.contains("Camiones")) {
                    ctrlFlota.agregarCamion(campos.get(0).getText(), campos.get(1).getText(), 
                        campos.get(2).getText(), campos.get(3).getText(), campos.get(4).getText(), 
                        modelPrincipal, modelAlertas);
                        
                } else if (tituloModulo.contains("Conductores")) {
                    ctrlFlota.agregarConductor(campos.get(0).getText(), campos.get(1).getText(), 
                        campos.get(2).getText(), modelPrincipal);
                        
                } else if (tituloModulo.contains("Mantenimientos")) {
                    int idCamionReal = Integer.parseInt(campos.get(0).getText().trim()); 
                    
                    String fecha = campos.get(1).getText().trim();
                    String tipo = campos.get(2).getText().trim();
                    String desc = campos.get(3).getText().trim();
                    
                    ctrlFlota.agregarMantenimiento(
                        idCamionReal, 
                        fecha, 
                        tipo, 
                        desc, 
                        "0", 
                        modelPrincipal
                    );
                    cargarDatosDesdeBase(); 

                } else if (tituloModulo.contains("Inventario de Equipos")) {
                    ctrlOficina.agregarEquipo(campos.get(0).getText(), campos.get(1).getText(), 
                        campos.get(2).getText(), modelPrincipal);
                        
                } else if (tituloModulo.contains("Gestión de Software")) {
                    int idEquipo = Integer.parseInt(campos.get(0).getText());
                    ctrlOficina.agregarSoftware(idEquipo, campos.get(1).getText(), 
                        campos.get(2).getText(), modelPrincipal);
                        
                } else if (tituloModulo.contains("Inventario de Piezas")) {
                    int stock = Integer.parseInt(campos.get(1).getText());
                    ctrlOficina.agregarPieza(
                        campos.get(0).getText(), // Nombre
                        stock,                   // Stock
                        campos.get(2).getText(), // Fecha Movimiento
                        campos.get(3).getText(), // Estado
                        modelPrincipal
                    );
                    
                } else if (tituloModulo.contains("Mantenimiento de Oficina")) {
                    int idEquipo = Integer.parseInt(campos.get(0).getText());
                    ctrlOficina.agregarMantenimiento(idEquipo, campos.get(1).getText(), 
                        campos.get(2).getText(), campos.get(3).getText(), modelPrincipal);
                }
                
                limpiarControles();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error de formato: Asegúrese de ingresar valores numéricos en las casillas de ID o Cantidades.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
            }
        });

        // 4. ELIMINAR 
        btnEliminar.addActionListener(e -> {
            int fila = tablePrincipal.getSelectedRow();
            if (fila >= 0) {
                try {
                    int filaCorrecta = tablePrincipal.convertRowIndexToModel(fila);
                    
                    // Capturamos el ID real de la columna 0
                    int id = Integer.parseInt(modelPrincipal.getValueAt(filaCorrecta, 0).toString());
                    
                    if (tituloModulo.contains("Camiones")) {
                        ctrlFlota.eliminarCamion(id, modelPrincipal, modelAlertas);
                    } else if (tituloModulo.contains("Conductores")) {
                        ctrlFlota.eliminarConductor(id, modelPrincipal);
                    } else if (tituloModulo.contains("Mantenimientos")) { 
                        ctrlFlota.eliminarMantenimiento(id, modelPrincipal);
                    } else if (tituloModulo.contains("Inventario de Equipos")) {
                        ctrlOficina.eliminarEquipo(id, modelPrincipal);
                    } else if (tituloModulo.contains("Gestión de Software")) {
                        ctrlOficina.eliminarSoftware(id, modelPrincipal);
                    } else if (tituloModulo.contains("Inventario de Repuestos")) {
                        ctrlOficina.eliminarPieza(id, modelPrincipal);
                    } else if (tituloModulo.contains("Mantenimiento de Oficina")) {
                        ctrlOficina.eliminarMantenimiento(id, modelPrincipal);
                    }
                    
                    limpiarControles();
                    cargarDatosDesdeBase(); 
                    
                } catch (Exception ex) {
                    System.err.println("Error al eliminar registro: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this, "Error al procesar la eliminación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un registro en la tabla para eliminar.");
            }
        });
    }

    private void limpiarControles() {
        for (JTextField f : campos) f.setText("");
        tablePrincipal.clearSelection();
    }
}