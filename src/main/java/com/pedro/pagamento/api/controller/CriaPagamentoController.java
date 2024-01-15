package com.pedro.pagamento.api.controller;

import com.pedro.pagamento.dto.response.MercadoPagoDto;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Pagamento;
import com.pedro.pagamento.repository.ClienteRepository;
import com.pedro.pagamento.repository.PagamentoRepository;
import com.pedro.pagamento.service.CadastroCompraService;
import com.pedro.pagamento.service.CadastroPagamentoService;
import com.pedro.pagamento.service.GerenciadorDePagamentoMercadoPagoService;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagar")
public class CriaPagamentoController {
  @Autowired GerenciadorDePagamentoMercadoPagoService gerenciadorDePagamentoMercadoPagoService;
  @Autowired CadastroCompraService cadastroCompraService;
  @Autowired ClienteRepository repository;
  @Autowired CadastroPagamentoService pagamentoService;

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.CREATED)
  public MercadoPagoDto criaPagamento(@PathVariable Long id) {
    return gerenciadorDePagamentoMercadoPagoService.processaPagamento(id);
  }

  @GetMapping("/status/{id}")
  public Pagamento buscaPagamentoStatus(@PathVariable Long id) {
    return cadastroCompraService.verificarStatusCompra(id);
  }

  @GetMapping("/pagadores")
  public List<Cliente> buscaPagadoresConfirmados() {

    return pagamentoService.getPagadores();
  }
}
