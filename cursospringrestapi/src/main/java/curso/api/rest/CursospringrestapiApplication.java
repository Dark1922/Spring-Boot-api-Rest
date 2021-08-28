package curso.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CursospringrestapiApplication implements WebMvcConfigurer{

	public static void main(String[] args)  {
		SpringApplication.run(CursospringrestapiApplication.class, args);
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		//libera acesso a todos controlles/endpoints
		registry.addMapping("usuario/**")
		.allowedMethods("*")//todos métodos
		.allowedOrigins("*"); //para todas origens
	}	
	//liberando o mapeamento de usuario para todas as origens

}
