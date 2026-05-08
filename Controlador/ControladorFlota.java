package Controlador;

import Dao.CamionDao;
import Dao.ConductorDao;
import Dao.MantenimientoDao;
import Modelo.Camion;
import Modelo.Conductor;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControladorFlota {
    private final CamionDao camionDao = new CamionDao();
    private final ConductorDao conductorDao = new ConductorDao();
    private final MantenimientoDao mantenimientoDao = new MantenimientoDao();

    // =================================================================
    // 1. GESTIÓN DE CAMIONES (CRUD COMPLETO)
    // =================================================================
    
    public void cargarCamiones(DefaultTableModel modeloPrincipal, DefaultTableModel modeloAlertas) {
    try {
        // 1. LIMPIEZA: Vaciamos ambas tablas antes de cargar datos frescos
        modeloPrincipal.setRowCount(0);
        if (modeloAlertas != null) {
            modeloAlertas.setRowCount(0);
        }

        // 2. LECTURA: Obtenemos los datos desde el DAO
        // Nota: Usamos obtenerSoloCamiones() que es el método que tienes en tu CamionDao.java
        List<Object[]> lista = camionDao.obtenerSoloCamiones(); 

        for (Object[] fila : lista) {
            // 3. LLENADO PRINCIPAL: Agregamos la fila a la tabla de gestión
            modeloPrincipal.addRow(fila);

            // 4. LÓGICA DE ALERTA (RF-10): Calculamos la diferencia de kilometraje
            // fila[4] es KM Actual, fila[5] es KM Último Mantenimiento
            try {
                double kmActual = Double.parseDouble(fila[4].toString());
                double kmUltimo = Double.parseDouble(fila[5].toString());
                double diferencia = kmActual - kmUltimo;

                // Si el exceso es >= 5000 km y estamos en la vista de Camiones
                if (diferencia >= 5000 && modeloAlertas != null) {
                    modeloAlertas.addRow(new Object[]{
                        fila[1] + " " + fila[2], // Marca y Modelo
                        String.format("%.1f km", diferencia), // Kilometraje de exceso
                        "PENDIENTE" // Estado de la alerta
                    });
                }
            } catch (Exception e) {
                // Si un dato no es numérico, saltamos esa fila para evitar que el sistema se caiga
                System.err.println("Error procesando alerta para ID " + fila[0] + ": " + e.getMessage());
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar datos desde la base de datos: " + e.getMessage());
    }
}

    public void agregarCamion(String mar, String mod, String an, String kmA, String kmU, DefaultTableModel m,DefaultTableModel a) {
        try {
            Camion c = new Camion();
            c.setMarca(mar); c.setModelo(mod);
            c.setAnio(Integer.parseInt(an));
            c.setKmActual(Double.parseDouble(kmA));
            c.setKmUltimoMantenimiento(Double.parseDouble(kmU));
            if (camionDao.insertarSoloCamion(c)) {
                cargarCamiones(m,a);
                JOptionPane.showMessageDialog(null, "Camión registrado.");
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()); }
    }

    public void actualizarCamion(int id, String mar, String mod, String an, String kmA, String kmU, DefaultTableModel m, DefaultTableModel a) {
        try {
            Camion c = new Camion();
            c.setId(id); c.setMarca(mar); c.setModelo(mod);
            c.setAnio(Integer.parseInt(an));
            c.setKmActual(Double.parseDouble(kmA));
            c.setKmUltimoMantenimiento(Double.parseDouble(kmU));
            if (camionDao.actualizarSoloCamion(c)) { // Se requiere crear en DAO
                cargarCamiones(m,a);
                JOptionPane.showMessageDialog(null, "Camión ID " + id + " actualizado.");
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()); }
    }

    public void eliminarCamion(int id, DefaultTableModel m, DefaultTableModel a) {
        try {
            if (camionDao.eliminar(id)) {
                cargarCamiones(m,a);
                JOptionPane.showMessageDialog(null, "Vehículo eliminado.");
            }
        } catch (SQLException e) { JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()); }
    }

    // =================================================================
    // 2. GESTIÓN DE CONDUCTORES (CRUD COMPLETO)
    // =================================================================

    public void cargarConductores(DefaultTableModel modelo) {
        try {
            modelo.setRowCount(0);
            List<Conductor> lista = conductorDao.obtenerTodos();
            for (Conductor c : lista) {
                modelo.addRow(new Object[]{c.getId(), c.getNombre(), c.getLicencia(), c.getTelefono()});
            }
        } catch (SQLException e) { System.err.println("Error carga conductores: " + e.getMessage()); }
    }

    public void agregarConductor(String nom, String lic, String tel, DefaultTableModel m) {
        try {
            if (conductorDao.insertar(nom, lic, tel)) { // Se requiere crear en DAO
                cargarConductores(m);
                JOptionPane.showMessageDialog(null, "Conductor registrado.");
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()); }
    }

    public void actualizarConductor(int id, String nom, String lic, String tel, DefaultTableModel m) {
        try {
            if (conductorDao.actualizar(id, nom, lic, tel)) { // Se requiere crear en DAO
                cargarConductores(m);
                JOptionPane.showMessageDialog(null, "Datos del conductor actualizados.");
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()); }
    }

    public void eliminarConductor(int id, DefaultTableModel m) {
        try {
            if (conductorDao.eliminar(id)) { // Se requiere crear en DAO
                cargarConductores(m);
                JOptionPane.showMessageDialog(null, "Conductor removido del sistema.");
            }
        } catch (Exception e) { System.err.println("Error: " + e.getMessage()); }
    }

    // =================================================================
    // 3. GESTIÓN DE MANTENIMIENTO (CRUD COMPLETO)
    // =================================================================

    public void cargarHistorial(DefaultTableModel modelo) {
    try {
        modelo.setRowCount(0);
        List<Object[]> lista = mantenimientoDao.obtenerHistorialCompleto(); 
        for (Object[] fila : lista) {
            // Estructura: ID, Fecha, Tipo, Descripcion, KmActual
            modelo.addRow(new Object[]{fila[0], fila[1], fila[2], fila[3], fila[4]});
        }
    } catch (SQLException e) { 
        System.err.println("Error carga historial: " + e.getMessage()); 
    }
}

public void agregarMantenimiento(int idCamion, String fecha, String tipo, String desc, String km, DefaultTableModel m) {
    try {
        double kilometraje = Double.parseDouble(km); // Convertimos el texto a número
        if (mantenimientoDao.insertarMantenimiento(idCamion, fecha, tipo, desc, kilometraje)) {
            cargarHistorial(m);
            JOptionPane.showMessageDialog(null, "Mantenimiento registrado a los " + km + " km.");
        }
    } catch (Exception e) { 
        JOptionPane.showMessageDialog(null, "Error (¿Kilometraje válido?): " + e.getMessage()); 
    }
}

public void actualizarMantenimiento(int idMant, String fecha, String tipo, String desc, String km, DefaultTableModel m) {
    try {
        double kilometraje = Double.parseDouble(km);
        if (mantenimientoDao.actualizarMantenimiento(idMant, fecha, tipo, desc, kilometraje)) {
            cargarHistorial(m);
            JOptionPane.showMessageDialog(null, "Registro actualizado correctamente.");
        }
    } catch (Exception e) { 
        JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage()); 
    }
}

    public void eliminarMantenimiento(int idMant, DefaultTableModel m) {
        try {
            if (mantenimientoDao.eliminar(idMant)) { // Se requiere crear en DAO
                cargarHistorial(m);
                JOptionPane.showMessageDialog(null, "Entrada de historial eliminada.");
            }
        } catch (Exception e) { System.err.println("Error: " + e.getMessage()); }
    }
}