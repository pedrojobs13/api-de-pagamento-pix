package com.pedro.pagamento.dto.mercadoPago;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PayerDTO {
  private String email;
  private String first_name;
  private String last_name;
  private IdentificationDTO identification;
}
