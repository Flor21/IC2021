package ejercicio.prueba;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Sumar dos numeros enteros cuyo resultado sea mayor a 10." );
    }
    
    public static boolean sumar(Integer A, Integer B)
    {
    	return ( A + B) >=10?true:false;
    }
}
