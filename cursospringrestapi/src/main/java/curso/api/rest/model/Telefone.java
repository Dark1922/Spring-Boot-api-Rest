package curso.api.rest.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("deprecation")
@Entity //classe persistente no banco de dados
@Getter
@Setter
public class Telefone implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String numero;

	@ManyToOne
	@ForeignKey(name = "usuario_id")
	private Usuario usuario;
}
