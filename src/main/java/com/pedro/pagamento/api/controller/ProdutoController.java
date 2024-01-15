package com.pedro.pagamento.api.controller;

import com.pedro.pagamento.repository.ProdutoRepository;
import com.pedro.pagamento.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
  @Autowired ProdutoRepository produtoRepository;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Produto inserir(@RequestBody Produto produto) {
    return produtoRepository.save(produto);
  }
}
