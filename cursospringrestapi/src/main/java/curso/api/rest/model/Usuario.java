package curso.api.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario  implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@EqualsAndHashCode.Include //fazer diferenciação de objetos comparar remover criar
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String login;
	
	private String password;
	
	private String nome;
	
	//relacionando com usuario no telefone , removal é pra remover usuario junto com telefone
	@OneToMany(mappedBy = "usuario" , 
			orphanRemoval = true, cascade = CascadeType.ALL) 
	private List<Telefone> telefones = new ArrayList<Telefone>();
}
