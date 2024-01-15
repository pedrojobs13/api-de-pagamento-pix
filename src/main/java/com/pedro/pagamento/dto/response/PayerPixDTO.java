package com.pedro.pagamento.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PayerPixDTO {
  private String FirstName;
  private String LastName;
}
