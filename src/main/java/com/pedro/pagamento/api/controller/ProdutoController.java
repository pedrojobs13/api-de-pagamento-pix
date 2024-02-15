package com.pedro.pagamento.api.controller;

import com.pedro.pagamento.api.controller.assembler.ProdutoModelAssembler;
import com.pedro.pagamento.api.controller.assembler.ProdutoModelDisassembler;
import com.pedro.pagamento.dto.controller.ProdutoModelDTO;
import com.pedro.pagamento.dto.controller.input.ProdutoInputDTO;
import com.pedro.pagamento.repository.ProdutoRepository;
import com.pedro.pagamento.model.Produto;
import com.pedro.pagamento.service.CadastroProdutoService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/produto")
public class ProdutoController {
  @Autowired private CadastroProdutoService produtoService;
  @Autowired private ProdutoRepository produtoRepository;
  @Autowired private ProdutoModelAssembler produtoModelAssembler;
  @Autowired private ProdutoModelDisassembler produtoModelDisassembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProdutoModelDTO inserir(@RequestBody @Valid ProdutoInputDTO produtoInputDTO) {
    Produto produto = produtoModelDisassembler.toDomainObject(produtoInputDTO);

    return produtoModelAssembler.toModel(produtoService.salvar(produto));
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProdutoModelDTO buscar(@PathVariable Long id) {
    Produto produto = produtoService.buscar(id);
    return produtoModelAssembler.toModel(produto);
  }


  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ProdutoModelDTO> listar() {
    return produtoModelAssembler.toCollectionModel(produtoRepository.findAll());
  }
}
