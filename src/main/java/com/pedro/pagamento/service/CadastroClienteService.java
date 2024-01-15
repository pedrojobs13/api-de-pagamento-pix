package com.pedro.pagamento.service;

import com.pedro.pagamento.repository.ClienteRepository;
import com.pedro.pagamento.repository.ProdutoRepository;
import com.pedro.pagamento.exception.ProdutoNaoEncontradoException;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastroClienteService {
  @Autowired ClienteRepository clienteRepository;
  @Autowired ProdutoRepository produtoRepository;

  public Cliente inserir(Cliente cliente) {

    Long produtoId = cliente.getProduto().getId();
    Produto produto =
        produtoRepository
            .findById(produtoId)
            .orElseThrow(
                () -> new ProdutoNaoEncontradoException("Não foi possível localizar o produto"));

    cliente.setProduto(produto);
    return clienteRepository.save(cliente);
  }
}
