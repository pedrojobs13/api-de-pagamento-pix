package com.pedro.pagamento.api.controller;

import com.pedro.pagamento.api.controller.assembler.ClienteModelAssembler;
import com.pedro.pagamento.dto.controller.ClienteModelDTO;
import com.pedro.pagamento.dto.response.MercadoPagoDto;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Pagamento;
import com.pedro.pagamento.repository.ClienteRepository;
import com.pedro.pagamento.service.VerificaEAtualizaPagamentoService;
import com.pedro.pagamento.service.CadastroPagamentoService;
import com.pedro.pagamento.service.GerenciadorDePagamentoMercadoPagoService;
import jakarta.validation.Valid;
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

  @GetMapping("/{codigo}")
  @ResponseStatus(HttpStatus.CREATED)
  public MercadoPagoDto criaPagamento(@PathVariable String codigo) {
    return gerenciadorDePagamentoMercadoPagoService.processaPagamento(codigo);
  }

  //    @ResponseStatus(HttpStatus.OK)
  //    @GetMapping("/status/{id}")
  //    public Pagamento buscaPagamentoStatus(@PathVariable Long id) {
  //      return verificaEAtualizaPagamentoService.verificarEAtualizaStatusDaCompra(id);
  //    }



  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/webhook")
  public void updateWebhookPagamentoStatus(
      @RequestParam("data.id") String dataId,
      @RequestParam String type,
      @RequestBody String jsonresponseBody) {

    verificaEAtualizaPagamentoService.recebeWebHookPagamento(jsonresponseBody);
  }

  @CrossOrigin(origins = "*")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/pagadores")
  public List<ClienteModelDTO> buscaPagadoresConfirmados() {
    return clienteModelAssembler.toCollectionModel(pagamentoService.getPagadores());
  }
}
