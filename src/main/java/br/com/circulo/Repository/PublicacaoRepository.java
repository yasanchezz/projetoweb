package br.com.circulo.Repository;

import br.com.circulo.model.Publicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PublicacaoRepository extends JpaRepository<Publicacao, Long> {

    // Ignorar maiúsculas e minúsculas
    List<Publicacao> findByTituloContainingIgnoreCase(String titulo);
}