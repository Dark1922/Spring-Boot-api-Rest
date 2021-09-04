package curso.api.rest;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler{

	//vai tratar os erros que termine com essas classes que emitem exceções
	@ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		String msg = "";
		
		//argumentos inválidos para algum método
		if(ex instanceof MethodArgumentNotValidException) {
			
			List<ObjectError> list = ((MethodArgumentNotValidException) ex)
					.getBindingResult().getAllErrors(); //converte os erro na lista e busca todos
			
			for (ObjectError objectError : list) { //varrer a lista

				msg += objectError.getDefaultMessage() + "\n"; //colocar as msg na string
			}
			
		}else {
			//se não só pega o erro da mensagem padrão
			msg = ex.getMessage();
		}
		
		ObjetoErro objetoErro = new ObjetoErro();
		objetoErro.setError(msg);
		objetoErro.setCode(status.value() + " ==> " + status.getReasonPhrase());
		
		return new 	ResponseEntity<Object>(objetoErro, headers, status);
	}
	
}
