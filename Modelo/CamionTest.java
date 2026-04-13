package Modelo;

import static org.junit.Assert.*;
import org.junit.Test;

public class CamionTest {

    // Lógica de Alerta
    @Test
    public void testRequiereMantenimiento() {
        Camion c = new Camion();
        c.setKmActual(10000);
        c.setKmUltimoMantenimiento(4000); // 6000 km recorridos
        assertTrue("Debería ser true porque superó los 5000km", c.requiereMantenimiento());
        
        c.setKmActual(5000);
        c.setKmUltimoMantenimiento(4000); // 1000 km recorridos
        assertFalse("Debería ser false porque no ha llegado al límite", c.requiereMantenimiento());
    }

    // Garantiza que el objeto guarda bien lo que recibe
    @Test
    public void testDatosCamion() {
        Camion c = new Camion();
        c.setMarca("Suzuki");
        c.setAnio(2017);
        
        assertEquals("Suzuki", c.getMarca());
        assertEquals(2017, c.getAnio());
    }

    // Lógica de limite (Exactamente 5000 km)
    @Test
    public void testLimiteExacto() {
        Camion c = new Camion();
        c.setKmActual(5000);
        c.setKmUltimoMantenimiento(0); 
        assertTrue("En el límite exacto de 5000 también debería alertar", c.requiereMantenimiento());
    }
}