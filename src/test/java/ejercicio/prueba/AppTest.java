package ejercicio.prueba;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertTrue;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppTest 
{
    private static App prueba;
    
    @Test
    public void sumarTest()
    {
    	assertTrue("La suma no es valida porque devuelve un numero menor a diez", prueba.sumaMayorADiez(5, 3));
    }
}
