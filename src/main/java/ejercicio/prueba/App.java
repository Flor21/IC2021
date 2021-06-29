package ejercicio.prueba;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@SpringBootApplication
public class App 
{
   /* public static void main( String[] args )
    {
        System.out.println( "Sumar dos numeros enteros cuyo resultado sea mayor a 10." );
    }
    */
	@RequestMapping("/ ")
	public String index(Model model) {
			return "index";
		}
	//faltaria llamar al metodo
	@RequestMapping("/resultadoPeticiones")
	public String formularioPeticiones(@RequestParam Map<String,String> requestParams, Model modelo) {
	 String n1 = requestParams.get("nro1");
	 String n2 = requestParams.get("nro2");
	 return "resultadoPeticiones";
	 
	}
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
    public static boolean sumaMayorADiez(Integer A, Integer B)
    {
    	return ( A + B) >=10?true:false;
    }
}
