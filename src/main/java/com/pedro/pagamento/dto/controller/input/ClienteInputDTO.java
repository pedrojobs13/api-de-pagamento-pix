package com.pedro.pagamento.dto.controller.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteInputDTO {

  @NotBlank private String nome;
  @NotBlank private String sobrenome;
  @Email private String email;

  @NotNull private BigDecimal valor;

  @Valid @NotNull private ProdutoIdInputDTO produto;
}
