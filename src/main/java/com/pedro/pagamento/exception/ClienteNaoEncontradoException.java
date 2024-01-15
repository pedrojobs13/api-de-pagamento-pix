package com.pedro.pagamento.exception;

public class ClienteNaoEncontradoException extends EntidadeNaoEncontradaException {
  private static final long serialVersionUID = 1L;

  public ClienteNaoEncontradoException(String message) {
    super(message);
  }
}
