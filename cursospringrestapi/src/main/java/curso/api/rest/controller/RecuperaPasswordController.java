package curso.api.rest.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.ObjetoErro;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import curso.api.rest.service.ServiceEnviaEmail;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/recuperar")
@AllArgsConstructor
@CrossOrigin
public class RecuperaPasswordController {

	private UsuarioRepository usuarioRepository;
	
	private ServiceEnviaEmail serviceEnviaEmail;
	
	
	@PostMapping("/")
	@ResponseBody
	public ResponseEntity<ObjetoErro> recuperar(@RequestBody Usuario login) throws Exception {
		
		ObjetoErro objetoErro = new ObjetoErro();
		
		//vai buscar o email q veio da tela
		Usuario user = usuarioRepository.findByLogin(login.getLogin()); 
		
		if(user == null) {
			
			objetoErro.setCode("400");/*Não encontrado*/
			objetoErro.setError("Usuário não encontrado.");
		}else {
			/*Gerar uma senha em formato de data e vai atualizar no banco de dados*/
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String senhaNova = dateFormat.format(Calendar.getInstance().getTime());
			
			String senhaCriptografada  = new BCryptPasswordEncoder().encode(senhaNova);
			
			/*Senha que vai set atualizada no banco criptografada*/
			usuarioRepository.updateSenha(senhaCriptografada, user.getId());
			
			serviceEnviaEmail
			.enviarEmail("Recuperação de senha", user.getEmail(),
					"Sua nova senha é: " + senhaNova); /*passa a senha pro usuario legivel/*
			
			/*Rotina de envio de E-mail*/
			objetoErro.setCode("200");/*Não encontrado*/
			objetoErro.setError("Acesso enviado para seu e-mail.");
		}
		
		return new  ResponseEntity<ObjetoErro>(objetoErro, HttpStatus.OK);
	}
}
