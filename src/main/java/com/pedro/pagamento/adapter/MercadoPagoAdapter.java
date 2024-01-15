package com.pedro.pagamento.adapter;

import com.pedro.pagamento.adapter.api.MercadoPagoApi;
import com.pedro.pagamento.dto.response.MercadoPagoDto;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MercadoPagoAdapter implements FormaDePagamentoAdapter {

  @Autowired MercadoPagoApi mercadoPagoApi;

  @Override
  public MercadoPagoDto realizaPagamento(Cliente cliente, Produto produto) {

    return mercadoPagoApi.payment(cliente, produto);
  }
}
