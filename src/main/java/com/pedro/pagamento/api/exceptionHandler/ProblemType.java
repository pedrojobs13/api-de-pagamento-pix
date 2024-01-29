package com.pedro.pagamento.api.exceptionHandler;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ProblemType {
  MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
  ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em Uso"),
  ERRO_DE_NEGOCIO("/erro-de-negocio", "Erro de Negócio"),
  RECURSO_NAO_ENCONTRADA("/recurso-nao-encontrada", "Recurso não encontrada"),
  PARAMETRO_INVALIDO("/parametro-invalido", "Esta uri não pertence ao domínio"),
  DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
  ERRO_DE_SISTEMA("/erro-de-sistema", "Erro no sitema");
  private String title;
  private String uri;

  ProblemType(String path, String title) {
    this.uri = "pagamento" + path;
    this.title = title;
  }
}
