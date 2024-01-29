package com.pedro.pagamento.dto.controller;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteModelDTO {
  private String codigo;
  private String nome;
  private String sobrenome;
  private String email;
  private BigDecimal valor;

  private ProdutoModelDTO produto;
}
