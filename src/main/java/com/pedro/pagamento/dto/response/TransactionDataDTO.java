package com.pedro.pagamento.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDataDTO {
  private String qrCode;
  private String ticketUrl;
}
