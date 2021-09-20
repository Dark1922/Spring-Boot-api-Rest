package curso.api.rest.repository;

import java.util.List;


import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.api.rest.model.Usuario;

@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("select u from Usuario u where u.login = ?1")
	Usuario findByLogin(String login);
    
	@Query("select u from Usuario u where u.email = ?1")
	Usuario findByEmail(String email); 
	
	@Query("select u from Usuario u where upper(trim(u.nome)) like %?1%")
	List<Usuario> findByNome(String nome);
	
	@Modifying //vai fazer modificação no banco de dados
	@Query(nativeQuery = true ,value = "update usuario set token = ?1 where login = ?2 ")
	void atualizarTokenUser(String token, String login );
	
	@Query(value = "Select constraint_name from information_schema.constraint_column_usage"
			+ " where table_name = 'usuarios_role' and column_name = 'role_id' and constraint_name"
			+ "<> 'unique_role_user';" , nativeQuery = true) //nativequery = sql puro
     String consultarConstraintRole();
	
	
	@Modifying
	@Query(nativeQuery = true , value = "insert into usuarios_role (usuario_id, role_id)"
			+ "	values(?1, (select id from role where nome_role = 'ROLE_USER'))")
	void insereAcessoRolePadrao(Long id);

	default Page<Usuario> findUserByNamePage(String nome, PageRequest pageRequest) {
		
		Usuario usuario = new Usuario();
		usuario.setNome(nome); //usuário que veio da tela pra consulta
		
		/*Configurando para pesquisar por nome e paginação, consultar onde existir parte desse nome contains() ignorando as case */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		/*prepara essa consulta juntando usuário com as configurações de consulta*/
		Example<Usuario> example = Example.of(usuario, exampleMatcher);
		
		/*Toda configuração de paginação e também de consulta*/
		return findAll(example, pageRequest); //retorno que veio da consulta						
	}
}
