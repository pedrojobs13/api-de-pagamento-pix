package com.pedro.pagamento.api.controller.assembler;

import com.pedro.pagamento.dto.controller.input.ClienteInputDTO;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Pagamento;
import com.pedro.pagamento.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteModelDisassembler {
  @Autowired ModelMapper modelMapper;

  public Cliente toDomainModel(ClienteInputDTO clienteInputDTO) {
    return modelMapper.map(clienteInputDTO, Cliente.class);
  }

  public void copyToDomainObject(ClienteInputDTO clienteInputDTO, Cliente cliente) {
    // tratar excessão do JPA
    cliente.setProduto(new Produto());
    cliente.setPagamento(new Pagamento());
    modelMapper.map(clienteInputDTO, cliente);
  }
}
