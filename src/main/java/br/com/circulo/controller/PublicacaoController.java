package br.com.circulo.controller;

import br.com.circulo.model.Categoria;
import br.com.circulo.model.Publicacao;
import br.com.circulo.model.StatusPublicacao;
import br.com.circulo.service.PublicacaoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/publicacoes")
public class PublicacaoController {

    private final PublicacaoService service;

    public PublicacaoController(PublicacaoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String titulo,
                         @RequestParam(required = false) Categoria categoria,
                         @RequestParam(defaultValue = "dataPublicacao") String sort,
                         @RequestParam(defaultValue = "desc") String direction,
                         Model model) {

        List<Publicacao> publicacoes = (titulo != null && !titulo.isBlank())
                ? service.pesquisarPorTitulo(titulo)
                : service.listarTodas();

        if (categoria != null) {
            publicacoes = publicacoes.stream()
                    .filter(p -> p.getCategoria() == categoria)
                    .collect(Collectors.toList());
        }

        publicacoes = ordenar(publicacoes, sort, direction);

        model.addAttribute("publicacoes", publicacoes);
        model.addAttribute("categorias", Arrays.asList(Categoria.values()));
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        return "publicacoes/lista";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("publicacao", new Publicacao());
        carregarListas(model);
        return "publicacoes/formulario";
    }

    @PostMapping
    public String salvar(@Valid Publicacao publicacao, BindingResult result,
                         Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            carregarListas(model);
            return "publicacoes/formulario";
        }
        service.salvar(publicacao);
        redirect.addFlashAttribute("mensagemSucesso", "Publicação salva com sucesso!");
        return "redirect:/publicacoes";
    }
    
    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("publicacao", service.buscarPorId(id));
            return "publicacoes/detalhes";
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/publicacoes";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("publicacao", service.buscarPorId(id));
            carregarListas(model);
            return "publicacoes/formulario";
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/publicacoes";
        }
    }

    @PostMapping("/editar/{id}")
    public String atualizar(@PathVariable Long id, @Valid Publicacao publicacao,
                            BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            carregarListas(model);
            return "publicacoes/formulario";
        }
        publicacao.setId(id);
        publicacao.setDataPublicacao(service.buscarPorId(id).getDataPublicacao());
        service.salvar(publicacao);
        redirect.addFlashAttribute("mensagemSucesso", "Publicação atualizada com sucesso!");
        return "redirect:/publicacoes";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.excluir(id);
            redirect.addFlashAttribute("mensagemSucesso", "Publicação excluída com sucesso!");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/publicacoes";
    }

    private void carregarListas(Model model) {
        model.addAttribute("categorias", Arrays.asList(Categoria.values()));
        model.addAttribute("statusList", Arrays.asList(StatusPublicacao.values()));
    }

    private List<Publicacao> ordenar(List<Publicacao> publicacoes, String sort, String direction) {
        Comparator<Publicacao> comparador;
        if ("titulo".equals(sort)) {
            comparador = Comparator.comparing(Publicacao::getTitulo, String.CASE_INSENSITIVE_ORDER);
        } else {
            comparador = Comparator.comparing(Publicacao::getDataPublicacao,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        }
        if (!"asc".equals(direction)) {
            comparador = comparador.reversed();
        }
        return publicacoes.stream().sorted(comparador).collect(Collectors.toList());
    }
}