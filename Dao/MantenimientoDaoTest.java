package Dao;

import Dao.MantenimientoDao;
import Modelo.Mantenimiento;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class MantenimientoDaoTest {

    @Test
    public void testInsertarYConsultar() {
        MantenimientoDao dao = new MantenimientoDao();
        Mantenimiento m = new Mantenimiento("Mantenimiento Preventivo", 5200.0);

        // 1. Probamos "Almacenar" (Create)
        boolean insertado = dao.insertar(m); 
        assertTrue("El registro debería guardarse en la base de datos", insertado);

        // 2. Probamos "Consultar" (Read)
        List<Mantenimiento> lista = dao.obtenerTodos();
        assertFalse("La lista no debería estar vacía después de insertar", lista.isEmpty());
    }
}