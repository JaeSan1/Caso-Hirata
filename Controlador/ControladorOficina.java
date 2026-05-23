package Controlador;

import Dao.Conexion;
import Dao.EquipoOficinaDao;
import Dao.MantenimientoEquipoDao;
import Dao.PiezaDao;
import Dao.SoftwareDao;
import Modelo.EquipoOficina;
import Modelo.MantenimientoEquipo;
import Modelo.Pieza;
import Modelo.SoftwareEquipo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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
                JOptionPane.showMessageDialog(null, "Mantenimiento registrado con éxito.");
                cargarMantenimientos(modelo); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en registro: " + e.getMessage());
        }
    }

    public void cargarMantenimientos(DefaultTableModel modelo) {
        try {
            modelo.setRowCount(0);
            List<Object[]> datos = mantDao.obtenerTodosParaTabla();
            for (Object[] fila : datos) {
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar mantenimientos: " + e.getMessage());
        }
    }

    public void agregarMantenimiento(int equipoId, String fecha, String tipo, String descripcion, DefaultTableModel modelo) {
        try {
            if (mantDao.insertarMantenimiento(equipoId, fecha, tipo, descripcion)) {
                JOptionPane.showMessageDialog(null, "Mantenimiento agregado con éxito.");
                cargarMantenimientos(modelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar mantenimiento: " + e.getMessage());
        }
    }

    public void actualizarMantenimiento(int id, int equipoId, String fecha, String tipo, String descripcion, DefaultTableModel modelo) {
        try {
            if (mantDao.actualizarMantenimiento(id, equipoId, fecha, tipo, descripcion)) {
                JOptionPane.showMessageDialog(null, "Mantenimiento actualizado con éxito.");
                cargarMantenimientos(modelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar mantenimiento: " + e.getMessage());
        }
    }

    public void eliminarMantenimiento(int id, DefaultTableModel modelo) {
        try {
            if (mantDao.eliminarMantenimiento(id)) {
                JOptionPane.showMessageDialog(null, "Mantenimiento eliminado con éxito.");
                cargarMantenimientos(modelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar mantenimiento: " + e.getMessage());
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
    public void ajustarStock(int piezaId, int cantidad, String fecha, String estado, DefaultTableModel modeloPiezas) {
        try {
            if (piezaDao.actualizarStock(piezaId, cantidad, fecha, estado)) {
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

    public void agregarEquipo(String nombre, String tipo, String estado, DefaultTableModel modelo) {
        try {
            if (equipoDao.insertarEquipo(nombre, tipo, estado)) {
                JOptionPane.showMessageDialog(null, "Equipo agregado con éxito.");
                cargarEquipos(modelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar equipo: " + e.getMessage());
        }
    }

    public void actualizarEquipo(int id, String nombre, String tipo, String estado, DefaultTableModel modelo) {
        try {
            if (equipoDao.actualizarEquipo(id, nombre, tipo, estado)) {
                JOptionPane.showMessageDialog(null, "Equipo actualizado con éxito.");
                cargarEquipos(modelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar equipo: " + e.getMessage());
        }
    }

    public void eliminarEquipo(int id, DefaultTableModel modelo) {
        try {
            if (equipoDao.eliminarEquipo(id)) {
                JOptionPane.showMessageDialog(null, "Equipo eliminado con éxito.");
                cargarEquipos(modelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar equipo: " + e.getMessage());
        }
    }

    public void cargarSoftware(DefaultTableModel modelo) {
        try {
            modelo.setRowCount(0);
            List<Object[]> datos = softDao.obtenerTodosParaTabla();
            for (Object[] fila : datos) {
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar software: " + e.getMessage());
        }
    }

    public void agregarSoftware(int equipoId, String nombreSoftware, String version, DefaultTableModel modelo) {
        try {
            if (softDao.insertarSoftware(equipoId, nombreSoftware, version)) {
                JOptionPane.showMessageDialog(null, "Software agregado con éxito.");
                cargarSoftware(modelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar software: " + e.getMessage());
        }
    }

    public void actualizarSoftware(int id, int equipoId, String nombreSoftware, String version, DefaultTableModel modelo) {
        try {
            if (softDao.actualizarSoftware(id, equipoId, nombreSoftware, version)) {
                JOptionPane.showMessageDialog(null, "Software actualizado con éxito.");
                cargarSoftware(modelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar software: " + e.getMessage());
        }
    }

    public void eliminarSoftware(int id, DefaultTableModel modelo) {
        try {
            if (softDao.eliminarSoftware(id)) {
                JOptionPane.showMessageDialog(null, "Software eliminado con éxito.");
                cargarSoftware(modelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar software: " + e.getMessage());
        }
    }

    public void cargarPiezas(DefaultTableModel modelo) {
        try {
            modelo.setRowCount(0);
            List<Object[]> datos = piezaDao.obtenerTodosParaTabla();
            for (Object[] fila : datos) {
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar piezas: " + e.getMessage());
        }
    }

    public void agregarPieza(String nombre, int stock, String fecha, String estado, DefaultTableModel modelo) {
        try {
            if (piezaDao.insertarPieza(nombre, stock, fecha, estado)) {
                JOptionPane.showMessageDialog(null, "Pieza agregada con éxito.");
                cargarPiezas(modelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar pieza: " + e.getMessage());
        }
    }

    public void actualizarPieza(int id, String nombre, int stock, String fecha, String estado, DefaultTableModel modelo) {
        try {
            if (piezaDao.actualizarPieza(id, nombre, stock, fecha, estado)) {
                JOptionPane.showMessageDialog(null, "Pieza actualizada con éxito.");
                cargarPiezas(modelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar pieza: " + e.getMessage());
        }
    }

    public void eliminarPieza(int id, DefaultTableModel modelo) {
        try {
            if (piezaDao.eliminarPieza(id)) {
                JOptionPane.showMessageDialog(null, "Pieza eliminada con éxito.");
                cargarPiezas(modelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar pieza: " + e.getMessage());
        }
    }
}
