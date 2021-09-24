package curso.api.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Profissao;
import curso.api.rest.repository.ProfissaoRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/profissao")
@AllArgsConstructor
@CrossOrigin
public class ProfissaoController {

	private ProfissaoRepository profissaoRepository;

	/* Carrega os dados das profissão do banco de dados para tela */

	
	@GetMapping("/")
	public ResponseEntity<List<Profissao>> profissoes() {

		List<Profissao> lista = profissaoRepository.findAll();
		return new ResponseEntity<List<Profissao>>(lista, HttpStatus.OK);
	}
}
