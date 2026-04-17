package Controlador;

import Dao.CamionDao;
import Modelo.Camion;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel; 

public class ControladorCamion {

    private final CamionDao camionDao; 

    public ControladorCamion() {
        this.camionDao = new CamionDao();
    }
    
    // Validación 
    private boolean validarCamposHirata(String marca, String modelo, String anio, String km, String nombreCond) {
        if (marca.trim().isEmpty() || modelo.trim().isEmpty() || anio.trim().isEmpty() || 
            km.trim().isEmpty() || nombreCond.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Complete los campos obligatorios: Marca, Modelo, Año, KM y Nombre del Conductor.", 
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void agregarCamion(String marca, String modelo, String anioStr, String kmStr, String kmUltimoMantStr, 
                            String fechaMantStr, String tipoMant, String descMant, String nombreCond, 
                            String licencia, String telefono, DefaultTableModel modeloTabla) {
        try {
            if (!validarCamposHirata(marca, modelo, anioStr, kmStr, nombreCond)) return;

            Camion camion = new Camion();
            camion.setMarca(marca);
            camion.setModelo(modelo);
            camion.setAnio(Integer.parseInt(anioStr));
            camion.setKmActual(Double.parseDouble(kmStr));
            camion.setKmUltimoMantenimiento(Double.parseDouble(kmUltimoMantStr));
            camion.setFechaUltimoMantenimiento(fechaMantStr); 

            if (camionDao.insertar(camion, tipoMant, descMant, nombreCond, licencia, telefono)) {
                cargarCamiones(modeloTabla); 
                JOptionPane.showMessageDialog(null, "Registro completo realizado en la flota Hirata.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar: " + e.getMessage());
        }
    }

    public void modificarCamion(int id, String marca, String modelo, String anioStr, String kmStr, 
                            String kmUltimoStr, String fechaMantStr, String tipoMant, String descMant, 
                            String nombreCond, String licencia, String telefono, DefaultTableModel modeloTabla) {
        try {
            if (!validarCamposHirata(marca, modelo, anioStr, kmStr, nombreCond)) return;

            Camion camion = new Camion();
            camion.setId(id); 
            camion.setMarca(marca);
            camion.setModelo(modelo);
            camion.setAnio(Integer.parseInt(anioStr));
            camion.setKmActual(Double.parseDouble(kmStr));
            camion.setKmUltimoMantenimiento(Double.parseDouble(kmUltimoStr)); 
            camion.setFechaUltimoMantenimiento(fechaMantStr);

            // Usamos el método de actualización 
            if (camionDao.actualizar(camion, tipoMant, descMant, nombreCond, licencia, telefono)) {
                cargarCamiones(modeloTabla); 
                JOptionPane.showMessageDialog(null, "Datos actualizados correctamente.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.getMessage());
        }
    }

    public void eliminarCamion(int filaSeleccionada, DefaultTableModel modeloTabla) {
        try {
            // Obtenemos el ID de la columna 0 
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar permanentemente el registro ID " + id + "?\nEsto borrará también sus mantenimientos asociados.");
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (camionDao.eliminar(id)) {
                    cargarCamiones(modeloTabla);
                    JOptionPane.showMessageDialog(null, "Registro eliminado de la base de datos.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        }
    }

    public void cargarCamiones(DefaultTableModel modeloTabla) {
        try {
            modeloTabla.setRowCount(0);
            // Obtenemos la lista de objetos combinados 
            List<Object[]> lista = camionDao.obtenerTodos();

            for (Object[] fila : lista) {
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos desde la DB: " + e.getMessage());
        }
    }
}