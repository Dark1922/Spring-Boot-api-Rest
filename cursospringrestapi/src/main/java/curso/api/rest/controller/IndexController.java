package curso.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Controller // arquitetura rest
@RequestMapping("/usuario")
@AllArgsConstructor
@CrossOrigin //qlq requisição sera permitido / acessar
public class IndexController {

	// se fosse cdi @Inject
	private UsuarioRepository usuarioRepository;

	// serviços restfull  Métodos Buscar por Todos
	@GetMapping()
	public ResponseEntity<List<Usuario>> listarTodos() {

		// retorna optional
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();

		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	

	/*               Métodos de Get buscar por id             */
	
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {

		// vai retorna a pessoa se der certo retorna um status ok , se n encontrar dá um
		// notofund
		return usuarioRepository.findById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}


	
	@GetMapping(value = "/metodoalex/{id}")
	public ResponseEntity<Usuario> poridAlex(@PathVariable Long id) {

		// retorna optional
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		// o retorno seria um relatorio
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	
	
	
	/*               Métodos de Post criação de Usuários             */

	@PostMapping()
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {

		for(int pos = 0; pos < usuario.getTelefones().size(); pos++) {
     	   usuario.getTelefones().get(pos).setUsuario(usuario); 
        }//varrendo no get do telefone aonde está setando o usuario que 
        //veio com parametro pai ,size é o tamanho
		
		//criptografia de senha
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getPassword());
		usuario.setPassword(senhaCriptografada);
		
		Usuario Usuariosalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(Usuariosalvo, HttpStatus.CREATED);

	}
	
	
	
	/*               Métodos de Put Atualização de Usuários       */
	
	@PutMapping("/{id}")
	public ResponseEntity<Usuario>atualizar(@PathVariable Long id,
			@RequestBody Usuario usuario) {
		
		if(!usuarioRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		usuario.setId(id); //força o usuario a ser atualizado na api
		
		for(int pos = 0; pos < usuario.getTelefones().size(); pos++) {
	     	   usuario.getTelefones().get(pos).setUsuario(usuario); 
	        }

		Usuario UsuarioAtualizar= usuarioRepository.save(usuario);

		return 	ResponseEntity.ok(UsuarioAtualizar);
		//ou return new ResponseEntity<Usuario>(Usuariosalvo, HttpStatus.OK);
 

	}
	
	
	/*               Métodos de Deletar os usuarios       */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover (@PathVariable Long id) {
		
		if(!usuarioRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		usuarioRepository.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
}
