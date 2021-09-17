package curso.api.rest;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)//n vai incluir pra campos nulo
@Getter
@Setter
public class ObjetoErro {

	private String error;
	private String code;
	private String titulo;
	private OffsetDateTime dataHora;
	private List<Campo> campos;
	
	@AllArgsConstructor
	@Getter
	public class Campo {
		private String nome;
		private String mensagem;
	}
}
