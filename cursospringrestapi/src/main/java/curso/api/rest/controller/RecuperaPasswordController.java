package curso.api.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.ObjetoErro;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/recuperar")
@AllArgsConstructor
@CrossOrigin
public class RecuperaPasswordController {

	private UsuarioRepository usuarioRepository;
	
	@PostMapping("/")
	@ResponseBody
	public ResponseEntity<ObjetoErro> recuperar(@RequestBody Usuario login) {
		
		ObjetoErro objetoErro = new ObjetoErro();
		
		//vai buscar o email q veio da tela
		Usuario user = usuarioRepository.findByLogin(login.getLogin()); 
		
		if(user == null) {
			
			objetoErro.setCode("400");/*Não encontrado*/
			objetoErro.setError("Usuário não encontrado.");
		}else {
			/*Rotina de envio de E-mail*/
			objetoErro.setCode("200");/*Não encontrado*/
			objetoErro.setError("Acesso enviado para seu e-mail.");
		}
		
		return new  ResponseEntity<ObjetoErro>(objetoErro, HttpStatus.OK);
	}
}
