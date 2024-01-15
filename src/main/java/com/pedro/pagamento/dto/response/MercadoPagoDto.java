package com.pedro.pagamento.dto.response;

import java.math.BigDecimal;
import javax.json.JsonNumber;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MercadoPagoDto {
  private final String status;
  private final Long id;
  private final String dataDeExpiracao;
  private final BigDecimal transactionAmount;
  private final PointOfInteractionDTO pointOfInteraction;
  private PayerPixDTO payerPixDTO;
}
