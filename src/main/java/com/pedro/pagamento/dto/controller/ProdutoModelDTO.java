package com.pedro.pagamento.dto.controller;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoModelDTO {
  private Long id;
  private String title;
  private String descricao;
  private String foto;
  private BigDecimal valor;
}
