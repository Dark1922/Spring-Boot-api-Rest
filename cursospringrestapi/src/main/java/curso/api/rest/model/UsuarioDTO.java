package curso.api.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private String userPassword;
	private String userLogin;
	private String userNome;
	private String userCpf;
	private String userEmail;
	private Date userDataNascimento;
	private List<Telefone> userTelefone; 
	private Profissao profissao;
	private BigDecimal userSalario;
	
	public UsuarioDTO(Usuario usuario) {

		this.userLogin = usuario.getLogin();
		this.userNome = usuario.getNome();
		this.userCpf = usuario.getCpf();
		this.userEmail = usuario.getEmail();
		this.userTelefone = usuario.getTelefones();
		this.userId = usuario.getId();
		this.userPassword = usuario.getPassword(); 
		this.userTelefone = usuario.getTelefones();
		this.userDataNascimento = usuario.getDataNascimento();
		this.profissao = usuario.getProfissao();
		this.userSalario = usuario.getSalario();
	}	

}
