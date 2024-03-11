package com.pedro.pagamento.api.controller;

import com.pedro.pagamento.api.controller.assembler.ClienteModelAssembler;
import com.pedro.pagamento.api.controller.assembler.PagamentoModelAssembler;
import com.pedro.pagamento.dto.controller.ClienteModelDTO;
import com.pedro.pagamento.dto.controller.PagamentoDTO;
import com.pedro.pagamento.dto.response.MercadoPagoDto;
import com.pedro.pagamento.model.Pagamento;
import com.pedro.pagamento.repository.ClienteRepository;
import com.pedro.pagamento.service.CadastroPagamentoService;
import com.pedro.pagamento.service.GerenciadorDePagamentoMercadoPagoService;
import com.pedro.pagamento.service.VerificaEAtualizaPagamentoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagar")
public class CriaPagamentoController {
  @Autowired GerenciadorDePagamentoMercadoPagoService gerenciadorDePagamentoMercadoPagoService;
  @Autowired VerificaEAtualizaPagamentoService verificaEAtualizaPagamentoService;
  @Autowired ClienteRepository repository;
  @Autowired CadastroPagamentoService pagamentoService;
  @Autowired ClienteModelAssembler clienteModelAssembler;
  @Autowired PagamentoModelAssembler pagamentoModelAssembler;

  @CrossOrigin
  @GetMapping("/{codigo}")
  @ResponseStatus(HttpStatus.CREATED)
  public MercadoPagoDto criaPagamento(@PathVariable String codigo) {
    return gerenciadorDePagamentoMercadoPagoService.processaPagamento(codigo);
  }


  @CrossOrigin
  @GetMapping("/status/{id}")
  public PagamentoDTO buscar(@PathVariable Long id) {
    Pagamento pagamento = verificaEAtualizaPagamentoService.verificarEAtualizaStatusDaCompra(id);
    return pagamentoModelAssembler.toModel(pagamento);
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/webhook")
  public void updateWebhookPagamentoStatus(
      @RequestParam("data.id") String dataId,
      @RequestParam String type,
      @RequestBody String jsonresponseBody) {

    verificaEAtualizaPagamentoService.recebeWebHookPagamento(jsonresponseBody);
  }

  @CrossOrigin
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/pagadores")
  public List<ClienteModelDTO> buscaPagadoresConfirmados() {
    return clienteModelAssembler.toCollectionModel(pagamentoService.getPagadores());
  }
}
