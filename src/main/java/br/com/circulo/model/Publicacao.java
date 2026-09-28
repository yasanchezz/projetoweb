package br.com.circulo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Publicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 100)
    private String titulo;

    @Size(max = 80)
    private String autora;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @NotBlank
    @Size(min = 20, max = 2000)
    @Column(length = 2000)
    private String conteudo;

    private LocalDateTime dataPublicacao;

    private Boolean anonima;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusPublicacao status;

    @Size(max = 150)
    private String tags;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutora() { return autora; }
    public void setAutora(String autora) { this.autora = autora; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public Boolean getAnonima() { return anonima; }
    public void setAnonima(Boolean anonima) { this.anonima = anonima; }

    public StatusPublicacao getStatus() { return status; }
    public void setStatus(StatusPublicacao status) { this.status = status; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
}