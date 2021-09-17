package curso.api.rest.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Telefone;

@Repository //operações diretas com banco de dados
@Transactional
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

}
