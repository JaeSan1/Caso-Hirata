package Controlador;

import Dao.EquipoOficinaDao;
import Dao.MantenimientoEquipoDao;
import Dao.PiezaDao;
import Dao.SoftwareDao;
import Modelo.EquipoOficina;
import Modelo.MantenimientoEquipo;
import Modelo.Pieza;
import Modelo.SoftwareEquipo;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControladorOficina {

    private final EquipoOficinaDao equipoDao;
    private final MantenimientoEquipoDao mantDao;
    private final PiezaDao piezaDao;
    private final SoftwareDao softDao;

    public ControladorOficina() {
        this.equipoDao = new EquipoOficinaDao();
        this.mantDao = new MantenimientoEquipoDao();
        this.piezaDao = new PiezaDao();
        this.softDao = new SoftwareDao();
    }

    // RF-06: Registrar Mantenimiento 
    public void registrarMantenimiento(int equipoId, String fechaStr, String tipo, String desc, DefaultTableModel modelo) {
        try {
            MantenimientoEquipo m = new MantenimientoEquipo();
            m.setEquipoId(equipoId);
            m.setFecha(Date.valueOf(fechaStr)); 
            m.setTipo(tipo);
            m.setDescripcion(desc);

            if (mantDao.registrarMantenimiento(m)) {
                JOptionPane.showMessageDialog(null, "Mantenimiento " + tipo + " registrado con éxito.");
                cargarEquipos(modelo); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en registro: " + e.getMessage());
        }
    }

    // RF-07: Actualización de Software
    public void registrarSoftware(int equipoId, String nombreSoft, String version, String fechaStr) {
        try {
            SoftwareEquipo s = new SoftwareEquipo();
            s.setEquipoId(equipoId);
            s.setNombreSoftware(nombreSoft);
            s.setVersion(version);
            s.setFechaActualizacion(Date.valueOf(fechaStr));

            if (softDao.registrarSoftware(s)) {
                JOptionPane.showMessageDialog(null, "Software actualizado y registrado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en software: " + e.getMessage());
        }
    }

    // RF-09: Control de Inventario de Piezas
    public void ajustarStock(int piezaId, int cantidad, DefaultTableModel modeloPiezas) {
        try {
            if (piezaDao.actualizarStock(piezaId, cantidad)) {
                cargarPiezas(modeloPiezas);
                JOptionPane.showMessageDialog(null, "Inventario actualizado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de stock: " + e.getMessage());
        }
    }

    // RF-08: Cargar Historial y Equipos
    public void cargarEquipos(DefaultTableModel modelo) {
        try {
            modelo.setRowCount(0);
            List<Object[]> datos = equipoDao.obtenerTodosParaTabla();
            for (Object[] fila : datos) {
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar equipos: " + e.getMessage());
        }
    }

    public void cargarPiezas(DefaultTableModel modelo) {
        try {
            modelo.setRowCount(0);
            List<Pieza> piezas = piezaDao.listarPiezas();
            for (Pieza p : piezas) {
                modelo.addRow(new Object[]{p.getId(), p.getNombre(), p.getStock()});
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar piezas: " + e.getMessage());
        }
    }
}
