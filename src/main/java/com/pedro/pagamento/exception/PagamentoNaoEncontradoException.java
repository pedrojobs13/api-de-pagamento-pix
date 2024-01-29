package com.pedro.pagamento.exception;

public class PagamentoNaoEncontradoException extends EntidadeNaoEncontradaException {
  public PagamentoNaoEncontradoException(String message) {
    super(message);
  }

  public PagamentoNaoEncontradoException(Long pagamentoId) {
    this(String.format("Não existe um cadastro de pagamento com código %d", pagamentoId));
  }
}
