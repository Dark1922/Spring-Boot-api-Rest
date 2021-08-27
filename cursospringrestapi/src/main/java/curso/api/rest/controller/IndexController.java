package curso.api.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Controller // arquitetura rest
@RequestMapping("/usuario")
@AllArgsConstructor
public class IndexController {

	// se fosse cdi @Inject
	private UsuarioRepository usuarioRepository;

	// serviços restfull
	@GetMapping(value = "/")
	public ResponseEntity<List<Usuario>> listarTodos() {

		// retorna optional
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();

		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {

		// vai retorna a pessoa se der certo retorna um status ok , se n encontrar dá um
		// notofund
		return usuarioRepository.findById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/relatoriopdf/{id}")
	public ResponseEntity<Usuario> relatorio(@PathVariable Long id) {

		// retorna optional
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		// o retorno seria um relatorio
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {

		Usuario Usuariosalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(Usuariosalvo, HttpStatus.CREATED);

	}
	
	@PostMapping("/outrometodo")
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario cadastrarOutroMetodo(@RequestBody Usuario usuario) {


		return usuarioRepository.save(usuario);

	}
}
