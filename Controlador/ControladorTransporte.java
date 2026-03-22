package controlador;

import java.awt.Checkbox;
import java.awt.TextField;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Transporte; 


public class conductores {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ConexionDB conexionDB; 

    public conductores() {
        this.conexionDB = new ConexionDB();
    }
    
    private boolean validarCamposObligatorios(String nombre, String licencia, String ubicacion, String fechaMantenimiento) {
        if (nombre.trim().isEmpty() || licencia.trim().isEmpty() || ubicacion.trim().isEmpty() || fechaMantenimiento.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                null, 
                "Por favor, complete los campos obligatorios: Nombre, Licencia, Ubicación GPS y Fecha de Mantenimiento.", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }

    public void agregarCamion(String patenteString, String conductor, String ubicacionGps, String fechamantenimientoString, boolean estado, DefaultTableModel modeloTabla) throws SQLException, NumberFormatException, DateTimeParseException {
        
        if (!validarCamposObligatorios(patenteString, conductor, ubicacionGps, fechamantenimientoString)) {
            return;
        }
        
        LocalDate localDate = LocalDate.parse(fechamantenimientoString, FORMATO_FECHA);
        Date sqlDate = Date.valueOf(localDate); 


        Camion camion = new Camion(0, patenteString, conductor, ubicacionGps, estado, sqlDate);

        if (conexionDB.insertar(camion)) {
            JOptionPane.showMessageDialog(null, "Camión registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void modificarCamion(int selectedRow, String patente, String conductor, String ubicacionGps, String fechamantenimientoString, boolean estado, DefaultTableModel modeloTabla) throws SQLException, NumberFormatException, DateTimeParseException {

        if (!validarCamposObligatorios(patente, conductor, ubicacionGps, fechamantenimientoString)) {
            return;
        }
        
        
        int idActualizar = (int) modeloTabla.getValueAt(selectedRow, 0); 
        
        LocalDate localDate = LocalDate.parse(fechamantenimientoString, FORMATO_FECHA);
        Date sqlDate = Date.valueOf(localDate);

        Camion camion = new Camion(idActualizar, patente, conductor, ubicacionGps, estado, sqlDate); 

        if (conexionDB.actualizar(camion, idActualizar)) { 

            modeloTabla.setValueAt(patente, selectedRow, 1);    
            modeloTabla.setValueAt(conductor, selectedRow, 2);    
            modeloTabla.setValueAt(ubicacionGps, selectedRow, 3); 
            modeloTabla.setValueAt(estado ? "Activo" : "Mantenimiento", selectedRow, 4);
            modeloTabla.setValueAt(fechamantenimientoString, selectedRow, 5); 

            JOptionPane.showMessageDialog(null, "Registro modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
             JOptionPane.showMessageDialog(null, "No se pudo actualizar el registro (ID no encontrado).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void eliminarCamion(DefaultTableModel modeloTabla, int selectedRow) throws SQLException {
        

        int idEliminar = (int) modeloTabla.getValueAt(selectedRow, 0); 
        
        int confirmacion = JOptionPane.showConfirmDialog(null, 
            "¿Está seguro de eliminar el camión con ID: " + idEliminar + "?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (conexionDB.eliminar(idEliminar)) { 
                modeloTabla.removeRow(selectedRow); 
                JOptionPane.showMessageDialog(null, "Registro eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar en la BD (ID no encontrado).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public void cargarCamiones(DefaultTableModel modeloTabla) throws SQLException {
        modeloTabla.setRowCount(0);

        List<Camion> listaCamiones = conexionDB.obtenerTodos(); 

        for (Camion camion : listaCamiones) {
            String fechaFormateada = camion.getFechaMantenimiento().toLocalDate().format(FORMATO_FECHA);
            
            modeloTabla.addRow(new Object[]{
                camion.getId(),
                camion.getPatente(),
                camion.getConductor(),
                camion.getUbicacionGps(),
                camion.isEstado() ? "Activo" : "Mantenimiento",
                fechaFormateada 
            });
        }
    }


    public void consultarRegistro(int selectedRow, DefaultTableModel modeloTabla, TextField txtPatente, TextField txtConductor, TextField txtUbicacion, TextField txtFechaMantenimiento, Checkbox chkActivo) throws SQLException {


        int idConsulta = (int) modeloTabla.getValueAt(selectedRow, 0); 
        txtPatente.setText(modeloTabla.getValueAt(selectedRow, 1).toString());
        txtConductor.setText(modeloTabla.getValueAt(selectedRow, 2).toString());
        txtUbicacion.setText(modeloTabla.getValueAt(selectedRow, 3).toString());
        
        String estadoTexto = modeloTabla.getValueAt(selectedRow, 4).toString();
        chkActivo.setState(estadoTexto.equals("Activo"));
        
        txtFechaMantenimiento.setText(modeloTabla.getValueAt(selectedRow, 5).toString());
    }
}
