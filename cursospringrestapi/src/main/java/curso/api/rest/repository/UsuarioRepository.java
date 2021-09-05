package curso.api.rest.repository;

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
	
	@Modifying //vai fazer modificação no banco de dados
	@Query(nativeQuery = true ,value = "update usuario set token = ?1 where login = ?2 ")
	void atualizarTokenUser(String token, String login );
	
}
