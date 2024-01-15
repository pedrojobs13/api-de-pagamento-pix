package com.pedro.pagamento.dto.mercadoPago;

import java.math.BigDecimal;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PaymentDTO {
  private String payment_method_id;
  private String description;
  private BigDecimal transaction_amount;
  private PayerDTO payer;
  private AdditionalInfoDTO additional_info;
}
