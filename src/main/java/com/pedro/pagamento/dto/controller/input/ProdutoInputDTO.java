package com.pedro.pagamento.dto.controller.input;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoInputDTO {

  @NotNull
  @Size(min = 3, max = 200)
  private String title;

  @NotNull
  @Size(min = 10, max = 200)
  private String descricao;

  @NotNull private String foto;

  @NotNull
  @Min(value = 5)
  private BigDecimal valor;
}
