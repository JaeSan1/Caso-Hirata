package controlador;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Dao.CamionDao; 
import Modelo.Camion; 

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

    public void agregarCamion(String marca, String modelo, String anioStr, String kmStr, int conductorId, DefaultTableModel modeloTabla) {
        try {
            if (!validarCamposHirata(marca, modelo, anioStr, kmStr)) return;

            Camion camion = new Camion();
            camion.setMarca(marca);
            camion.setModelo(modelo);
            camion.setAnio(Integer.parseInt(anioStr));
            camion.setKmActual(Double.parseDouble(kmStr));
            camion.setConductorId(conductorId);

            if (camionDao.insertar(camion)) {
                cargarCamiones(modeloTabla); 
                JOptionPane.showMessageDialog(null, "Vehículo registrado en la flota Hirata.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void modificarCamion(int id, String marca, String modelo, String anioStr, String kmStr, int conductorId, DefaultTableModel modeloTabla) {
        try {
            if (!validarCamposHirata(marca, modelo, anioStr, kmStr)) return;

            Camion camion = new Camion();
            camion.setId(id);
            camion.setMarca(marca);
            camion.setModelo(modelo);
            camion.setAnio(Integer.parseInt(anioStr));
            camion.setKmActual(Double.parseDouble(kmStr));
            camion.setConductorId(conductorId);

            if (camionDao.actualizar(camion)) {
                cargarCamiones(modeloTabla);
                JOptionPane.showMessageDialog(null, "Datos actualizados correctamente.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.getMessage());
        }
    }


    public void eliminarCamion(int id, DefaultTableModel modeloTabla) {
        try {
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
            StringBuilder alertas = new StringBuilder("⚠️ ALERTAS DE MANTENIMIENTO (5000km):\n");
            boolean hayAlertas = false;

            for (Camion c : lista) {
                modeloTabla.addRow(new Object[]{
                    c.getId(), c.getMarca(), c.getModelo(), c.getAnio(), c.getKmActual()
                });

                double diferencia = c.getKmActual() - c.getKmUltimoMantenimiento();
                if (diferencia >= 5000) {
                    alertas.append("- ").append(c.getMarca()).append(" ").append(c.getModelo())
                           .append(" (Exceso: ").append(diferencia - 5000).append(" km)\n");
                    hayAlertas = true;
                }
            }

            if (hayAlertas) {
                JOptionPane.showMessageDialog(null, alertas.toString(), "Mantenimiento Preventivo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos: " + e.getMessage());
        }
    }
}