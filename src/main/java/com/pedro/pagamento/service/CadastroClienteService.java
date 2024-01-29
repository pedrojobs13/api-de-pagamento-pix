package com.pedro.pagamento.service;

import com.pedro.pagamento.repository.ClienteRepository;
import com.pedro.pagamento.repository.ProdutoRepository;
import com.pedro.pagamento.exception.ProdutoNaoEncontradoException;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroClienteService {
  @Autowired ClienteRepository clienteRepository;
  @Autowired ProdutoRepository produtoRepository;
  private static String MSG_PRODUTO_NAO_ENCONTRADO = "Produto de id [%d] não foi localizado";

  @Transactional
  public Cliente inserir(Cliente cliente) {
    Long produtoId = cliente.getProduto().getId();
    Produto produto = buscarProduto(produtoId);
    cliente.setProduto(produto);
    return clienteRepository.save(cliente);
  }

  private Produto buscarProduto(Long produtoId) {
    return produtoRepository
        .findById(produtoId)
        .orElseThrow(
            () ->
                new ProdutoNaoEncontradoException(
                    String.format(MSG_PRODUTO_NAO_ENCONTRADO, produtoId)));
  }
}
