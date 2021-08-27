package curso.api.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Controller //arquitetura rest
@RequestMapping("/usuario")
@AllArgsConstructor
public class IndexController {

	//se fosse cdi @Inject
	private UsuarioRepository usuarioRepository;
	
	//serviço restfull
		@GetMapping(value = "/{id}", produces = "application/json")
		public ResponseEntity<Usuario> init(@PathVariable Long id)  {
			
			//retorna optional
			Optional<Usuario> usuario = usuarioRepository.findById(id);
			
			return new ResponseEntity(usuario.get(), HttpStatus.OK);
		}
		
				@GetMapping(value = "/", produces = "application/json")
				public ResponseEntity<List<Usuario>> listarTodos()  {
					
					//retorna optional
				    List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
					
					return new ResponseEntity<List<Usuario>>(list,HttpStatus.OK);
				}
	
}
