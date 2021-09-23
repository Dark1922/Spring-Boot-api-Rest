package curso.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Profissao;

@Repository
public interface ProfissaoRepository extends JpaRepository<Profissao, Long> {

}
