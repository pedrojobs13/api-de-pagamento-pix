package com.pedro.pagamento.exception;

public abstract class EntidadeNaoEncontradaException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public EntidadeNaoEncontradaException(String message) {
    super(message);
  }
}
