package curso.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@EnableCaching //habilitando o cache
public class CursospringrestapiApplication implements WebMvcConfigurer{

	public static void main(String[] args)  {
		SpringApplication.run(CursospringrestapiApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("admin"));
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		//libera acesso a todos controlles/endpoints
		registry.addMapping("usuario/**")
		.allowedMethods("*")//todos métodos
		.allowedOrigins("*"); //para todas origens
		
		registry.addMapping("profissao/**")
		.allowedMethods("*")//todos métodos
		.allowedOrigins("*");
	}	
	//liberando o mapeamento de usuario para todas as origens

}
