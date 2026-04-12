package Modelo; 
import Modelo.Camion;

import static org.junit.Assert.*;
import org.junit.Test;

public class CamionTest {

    @Test
    public void testAlertaMantenimiento() {
        // Simulamos un camión que ha recorrido 7000km (Excede los 5000)
        Camion camion = new Camion();
        camion.setKmActual(9000.0);
        camion.setKmUltimoMantenimiento(2000.0);
        
        // Esta prueba pasa si el método devuelve true
        assertTrue("Debería pedir mantenimiento", camion.requiereMantenimiento());
        
        // Simulamos un camión con solo 1000km de uso
        camion.setKmActual(3000.0);
        camion.setKmUltimoMantenimiento(2000.0);
        
        // Esta prueba pasa si el método devuelve false
        assertFalse("No debería pedir mantenimiento todavía", camion.requiereMantenimiento());
    }
}