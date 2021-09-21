package curso.api.rest.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario  implements UserDetails{ //ja tem o serializable e os metodos do springsecurity

	private static final long serialVersionUID = 1L;

	@Id
	@EqualsAndHashCode.Include //fazer diferenciação de objetos comparar remover criar
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank(message = "Infome um login.")
	@Size(max = 60, min = 4)
	@Column(unique = true) //login é único
	private String login;
	
	@NotBlank(message = "Infome uma senha.")
	@Size(max = 150, min = 6)
	@Column(name = "senha")
	private String password;
	 
	@NotBlank
	@CPF(message = "Informe um Cpf válido.")
	private String cpf;
	
	@NotBlank(message = "Infome um Nome.")
	@Size(max = 255)
	private String nome;
	
	@JsonFormat(timezone = "GMT-3",pattern = "dd/MM/yyyy") //devolve os dados para tela nesse formato
	@Temporal(TemporalType.DATE) //tipo de formato que quer q salve no banco de dados
	@DateTimeFormat(iso = ISO.DATE, pattern = "dd/MM/yyyy") //vem da tela pro backend tipo de data q ta recebendo
	private Date dataNascimento;
	
	/*
	@NotBlank
	private String cep;
	
	private String logradouro;
	private String complemento;
	private String bairro;
	private String localidade;
	private String uf;
	*/
	
	@Email(message = "Informe um email válido")
	@NotBlank(message = "E-mail é obrigatório")
	@Size(max = 255)
	@Column(unique = true)
	private String email;
	
	@JsonIgnore
	private String token = "";
	
	//relacionando com usuario no telefone , removal é pra remover usuario junto com telefone
	@OneToMany(mappedBy = "usuario" , 
			orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY) 
	private List<Telefone> telefones = new ArrayList<Telefone>();
	
    @OneToMany(fetch = FetchType.EAGER) //um usuario pode ter varios papeis
    @JoinTable(name = "usuarios_role", uniqueConstraints = @UniqueConstraint(
    		columnNames  = {"usuario_id" , "role_id"} , name = "unique_role_user"),
    joinColumns = @JoinColumn(name = "usuario_id" , referencedColumnName = "id", table = "usuario", unique = false,
    foreignKey = @ForeignKey(name = "usuario_fk", value = ConstraintMode.CONSTRAINT)),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", table = "role", unique = false,updatable = false,
    foreignKey = @ForeignKey (name = "role_fk", value = ConstraintMode.CONSTRAINT)))
	private List<Role> roles = new ArrayList<Role>(); //papeis de acesso
	
    
   

	//autorizações são os acessos do usuario ROLE_ADMIN GERENTE etc
    @Override
    @JsonIgnore
	public Collection<Role> getAuthorities() {
		return roles;
	}

    @JsonIgnore
	@Override
	public String getUsername() {
		return this.login;
	}
	@Override
	public String getPassword() {
		return this.password;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
}
