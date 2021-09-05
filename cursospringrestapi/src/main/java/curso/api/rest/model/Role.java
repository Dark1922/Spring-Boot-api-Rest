package curso.api.rest.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter       //nome do sequenciador de 1 em 1 e começo pelo 1
@SequenceGenerator(name = "seq_role" , sequenceName = "seq_role", allocationSize = 1,initialValue = 1)
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id                                                //vai pegar a geração acima
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_role")
	private Long id;
	
	private String nomeRole; //Papel exemplo ROLE_ADMIN ROLE_GERENTE ETC

	@Override
	public String getAuthority() { //Retorna o nome do papel, acesso ou autorização exmp ROLE_ADMIN
		return this.nomeRole;
	}
	
	
}
