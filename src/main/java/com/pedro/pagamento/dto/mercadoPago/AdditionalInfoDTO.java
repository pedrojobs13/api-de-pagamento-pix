package com.pedro.pagamento.dto.mercadoPago;

import java.util.List;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AdditionalInfoDTO {
  private List<ItemDTO> items;
  private AdditionalPayer payer;
}
