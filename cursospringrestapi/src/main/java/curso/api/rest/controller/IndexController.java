package curso.api.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity init() {
		
		return new ResponseEntity("Olá Rest Spring boot", HttpStatus.OK);
	}
	
}
