package ejercicio.prueba;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private static App prueba;
    
    @Test
    public void sumarTest()
    {
    	assertTrue("La suma no es valida porque devuelve un numero menor a diez", prueba.sumar(5, 5));
    }
}
