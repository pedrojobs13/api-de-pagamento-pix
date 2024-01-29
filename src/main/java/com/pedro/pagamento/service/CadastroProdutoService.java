package com.pedro.pagamento.service;

import com.pedro.pagamento.exception.EntidadeEmUsoException;
import com.pedro.pagamento.exception.ProdutoNaoEncontradoException;
import com.pedro.pagamento.model.Produto;
import com.pedro.pagamento.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroProdutoService {
  @Autowired private ProdutoRepository produtoRepository;
  private static final String MSG_PRODUTO_NAO_ENCONTRADO =
      "O produto de id [%d] não foi possível ser localizado";
  private static final String MSG_PRODUTO_EM_USO = "Entidade em uso";

  @Transactional
  public Produto salvar(Produto produto) {
    return produtoRepository.save(produto);
  }

  public Produto buscar(Long id) {
    return produtoRepository
        .findById(id)
        .orElseThrow(() -> new ProdutoNaoEncontradoException(MSG_PRODUTO_NAO_ENCONTRADO));
  }

  public void apagar(Long id) {
    try {
      produtoRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ProdutoNaoEncontradoException(String.format(MSG_PRODUTO_NAO_ENCONTRADO, id));
    } catch (DataIntegrityViolationException e) {
      throw new EntidadeEmUsoException(MSG_PRODUTO_EM_USO);
    }
  }
}
