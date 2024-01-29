package com.pedro.pagamento.api.controller.assembler;

import com.pedro.pagamento.dto.controller.ClienteModelDTO;
import com.pedro.pagamento.model.Cliente;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteModelAssembler {
  @Autowired ModelMapper modelMapper;

  public ClienteModelDTO toModel(Cliente cliente) {
    return modelMapper.map(cliente, ClienteModelDTO.class);
  }

  public List<ClienteModelDTO> toCollectionModel(List<Cliente> clientes) {
    return clientes.stream().map(cliente -> toModel(cliente)).collect(Collectors.toList());
  }
}
