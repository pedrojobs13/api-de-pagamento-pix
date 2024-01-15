package com.pedro.pagamento.api.controller;

import com.pedro.pagamento.repository.ClienteRepository;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.service.CadastroClienteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
  @Autowired private CadastroClienteService cadastroClienteService;
  @Autowired private ClienteRepository clienteRepository;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Cliente inserir(@RequestBody Cliente cliente) {
    return cadastroClienteService.inserir(cliente);
  }
}
