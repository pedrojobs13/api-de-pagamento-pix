package com.pedro.pagamento.dto.mercadoPago;

import java.math.BigDecimal;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ItemDTO {
  private Long id;
  private String title;
  private String description;
  private String picture_url;
  private BigDecimal unit_price;
}
