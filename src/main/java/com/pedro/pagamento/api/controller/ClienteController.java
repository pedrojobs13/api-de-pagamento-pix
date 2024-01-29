package com.pedro.pagamento.api.controller;

import com.pedro.pagamento.api.controller.assembler.ClienteModelAssembler;
import com.pedro.pagamento.api.controller.assembler.ClienteModelDisassembler;
import com.pedro.pagamento.dto.controller.ClienteModelDTO;
import com.pedro.pagamento.dto.controller.input.ClienteInputDTO;
import com.pedro.pagamento.repository.ClienteRepository;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.service.CadastroClienteService;
import jakarta.validation.Valid;
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
  @Autowired private ClienteModelAssembler clienteModelAssembler;
  @Autowired private ClienteModelDisassembler clienteModelDisassembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ClienteModelDTO inserir(@RequestBody @Valid ClienteInputDTO clienteInputDTO) {

    Cliente cliente = clienteModelDisassembler.toDomainModel(clienteInputDTO);

    return clienteModelAssembler.toModel(cadastroClienteService.inserir(cliente));
  }
}
