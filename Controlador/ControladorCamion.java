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
    
    private boolean validarCamposHirata(String marca, String modelo, String anio, String km) {
        if (marca.trim().isEmpty() || modelo.trim().isEmpty() || anio.trim().isEmpty() || km.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Complete los campos: Marca, Modelo, Año y Kilometraje.", 
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void agregarCamion(String marca, String modelo, String anioStr, String kmStr, String kmUltimoMantStr, String fechaMantStr, String tipo,String descripcion  int conductorId, String nombre, String licencia, String telefono DefaultTableModel modeloTabla) {
        try {
            if (!validarCamposHirata(marca, modelo, anioStr, kmStr)) return;

            Camion camion = new Camion();
            camion.setMarca(marca);
            camion.setModelo(modelo);
            camion.setAnio(Integer.parseInt(anioStr));
            camion.setKmActual(Double.parseDouble(kmStr));
            camion.setKmUltimoMantenimiento(Double.parseDouble(kmUltimoMantStr));
            camion.setFechaUltimoMantenimiento(fechaMantStr); 
            camion.setTipo(tipo);
            camion.setDescripcion(descripcion);
            camion.setConductorId(conductorId);
            camion.setNombre(nombre);
            camion.setLicencia(licencia);
            camion.setTelefono(telefono);

            if (camionDao.insertar(camion)) {
                cargarCamiones(modeloTabla); 
                JOptionPane.showMessageDialog(null, "Vehículo registrado en la flota Hirata.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void modificarCamion(int id, String marca, String modelo, String anioStr, String kmStr, String kmUltimoStr, String fechaMantStr, int conductorId, DefaultTableModel modeloTabla) {
        try {
            if (!validarCamposHirata(marca, modelo, anioStr, kmStr)) return;

            Camion camion = new Camion();
            camion.setId(id); 
            camion.setMarca(marca);
            camion.setModelo(modelo);
            camion.setAnio(Integer.parseInt(anioStr));
            camion.setKmActual(Double.parseDouble(kmStr));
            camion.setKmUltimoMantenimiento(Double.parseDouble(kmUltimoStr)); 
            camion.setFechaUltimoMantenimiento(fechaMantStr); //  fecha actualizada
            camion.setConductorId(conductorId);

            if (camionDao.actualizar(camion)) {
                cargarCamiones(modeloTabla); 
                JOptionPane.showMessageDialog(null, "Camión actualizado correctamente.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.getMessage());
        }
    }

    public void eliminarCamion(int filaSeleccionada, DefaultTableModel modeloTabla) {
        try {
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar camión ID " + id + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (camionDao.eliminar(id)) {
                    cargarCamiones(modeloTabla);
                    JOptionPane.showMessageDialog(null, "Eliminado de la flota.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        }
    }

    public void cargarCamiones(DefaultTableModel modeloTabla) {
        try {
            modeloTabla.setRowCount(0);
            List<Camion> lista = camionDao.obtenerTodos();

            for (Camion c : lista) {
                modeloTabla.addRow(new Object[]{
                    c.getId(), 
                    c.getMarca(), 
                    c.getModelo(), 
                    c.getAnio(), 
                    c.getKmActual(),
                    c.getKmUltimoMantenimiento(),
                    c.getFechaUltimoMantenimiento(), 
                    c.getConductorId()           
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos: " + e.getMessage());
        }
    }
}