package ejercicio.prueba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App 
{
   /* public static void main( String[] args )
    {
        System.out.println( "Sumar dos numeros enteros cuyo resultado sea mayor a 10." );
    }
    */
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
    public static boolean sumar(Integer A, Integer B)
    {
    	return ( A + B) >=10?true:false;
    }
}
