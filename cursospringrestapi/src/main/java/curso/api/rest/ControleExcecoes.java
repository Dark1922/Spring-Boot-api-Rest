package curso.api.rest;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
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
	//interceptar erros mais comuns no projeto
	@ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		String msg = "";
		
		//argumentos inválidos para as entidades
		
		
		if(ex instanceof MethodArgumentNotValidException) {
			
			List<ObjectError> list = ((MethodArgumentNotValidException) ex)
					.getBindingResult().getAllErrors(); //converte os erro na lista e busca todos
			
			for (ObjectError objectError : list) { //varrer a lista

				msg += objectError.getDefaultMessage() + ", "; //colocar as msg na string
			}
			
		}else {
			//se não só pega o erro da mensagem padrão
			msg = ex.getMessage();
		}
		
		ObjetoErro objetoErro = new ObjetoErro();
		objetoErro.setError(msg);
		objetoErro.setCode(status.value() + " ==> " + status.getReasonPhrase());
		objetoErro.setDataHora(OffsetDateTime.now()); //na hora que ocorreu 
		objetoErro.setTitulo("Um ou mais campos estão inválidos. Faça o preenchimento corretamente e tente novamente");
		
		return new 	ResponseEntity<Object>(objetoErro, headers, status);
	}
	
	//tratamento da maioria dos erros a nível do banco de dados
	@ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class,
		SQLException.class})
	protected ResponseEntity<Object> HandleExceptionDataIntegry(Exception ex) {
		
		String msg = "";
		
		if(ex instanceof DataIntegrityViolationException) {
			msg = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
		}
		else if (ex instanceof ConstraintViolationException) {
			msg = ((ConstraintViolationException) ex).getCause().getCause().getMessage();
		}
		else if(ex instanceof SQLException) {
			msg = ((SQLException) ex).getCause().getCause().getMessage();
		}
		
		else {
			msg = ex.getMessage();
		}
		
		ObjetoErro objetoErro = new ObjetoErro();
		objetoErro.setError(msg);
		//código do erro mais legível do banco de dados.
		objetoErro.setCode(HttpStatus.INTERNAL_SERVER_ERROR + " ==> " +
		HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		
		return new ResponseEntity<Object>(objetoErro, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
