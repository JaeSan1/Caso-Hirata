package Modelo;

import static org.junit.Assert.*;
import org.junit.Test;

public class MantenimientoTest {

    @Test
    public void testDatosMantenimiento() {
        // Creamos un mantenimiento de prueba
        Mantenimiento m = new Mantenimiento();
        
        m.setTipo("Cambio de Frenos");
        m.setKmAlMomento(5500.0); 

        assertEquals("Cambio de Frenos", m.getTipo());
        assertEquals(5500.0, m.getKmAlMomento(), 0.001); 
    }
}