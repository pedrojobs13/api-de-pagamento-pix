package com.pedro.pagamento.adapter;

import com.pedro.pagamento.dto.response.MercadoPagoDto;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Produto;
import org.springframework.http.ResponseEntity;

public interface FormaDePagamentoAdapter {
  MercadoPagoDto realizaPagamento(Cliente cliente, Produto produto);
}
