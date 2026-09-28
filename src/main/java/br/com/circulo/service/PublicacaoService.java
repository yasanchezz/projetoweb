package br.com.circulo.service;

import br.com.circulo.model.Publicacao;
import br.com.circulo.Repository.PublicacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PublicacaoService {

    private final PublicacaoRepository repository;

    public PublicacaoService(PublicacaoRepository repository) {
        this.repository = repository;
    }

    public List<Publicacao> listarTodas() {
        return repository.findAll();
    }

    public Publicacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publicação não encontrada."));
    }

    public Publicacao salvar(Publicacao publicacao) {
        if (publicacao.getId() == null) {
            publicacao.setDataPublicacao(LocalDateTime.now());
        }
        return repository.save(publicacao);
    }

    public void excluir(Long id) {
        Publicacao publicacao = buscarPorId(id);
        repository.delete(publicacao);
    }

    public List<Publicacao> pesquisarPorTitulo(String titulo) {
        return repository.findByTituloContainingIgnoreCase(titulo);
    }
}