package com.pedro.pagamento.exception;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException {

  private static final long serialVersionUID = 1L;

  public ProdutoNaoEncontradoException(String message){
    super(message);
  }
}
