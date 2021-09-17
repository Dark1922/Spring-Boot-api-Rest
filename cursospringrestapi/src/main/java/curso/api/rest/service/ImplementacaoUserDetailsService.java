package curso.api.rest.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ImplementacaoUserDetailsService implements UserDetailsService{

	private UsuarioRepository usuarioRepository;
	
	private JdbcTemplate jdbcTemplate; //executa sql puro
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//consultar no banco o usuario
		
		Usuario usuario = usuarioRepository.findByLogin(username);
		
		if(usuario == null) {
			throw new UsernameNotFoundException("Usuário não foi encontrado"); //username não encontrado
		}
		
		return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());
		//usuario tem que passar login senha e suas autoridade
	}

	public void insereAcessoPadrão(Long id) {
		//descobri qual é a constraint de restrição
		String constraint  = usuarioRepository.consultarConstraintRole(); //busca o nome da constrait
		
		if(constraint != null) { //se ela realmente desistir vai dar o comando de remover ela no banco
		
		//remove a constraint com nome que foi pego na consulta
		jdbcTemplate.execute("alter table usuarios role DROP CONSTRAINT " + constraint);
		
		//insere os acesso padrão
		usuarioRepository.insereAcessoRolePadrao(id);
		}
	}

}
