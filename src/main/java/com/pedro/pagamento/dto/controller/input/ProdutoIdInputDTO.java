package com.pedro.pagamento.dto.controller.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoIdInputDTO {
  @NotNull
  private Long id;
}
