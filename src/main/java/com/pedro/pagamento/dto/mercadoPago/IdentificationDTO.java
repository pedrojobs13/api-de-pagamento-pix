package com.pedro.pagamento.dto.mercadoPago;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class IdentificationDTO {
  private String type;
  private String number;
}
