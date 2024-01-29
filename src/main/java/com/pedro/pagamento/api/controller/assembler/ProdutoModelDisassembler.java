package com.pedro.pagamento.api.controller.assembler;

import com.pedro.pagamento.dto.controller.input.ProdutoInputDTO;
import com.pedro.pagamento.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProdutoModelDisassembler {
  @Autowired private ModelMapper modelMapper;

  public Produto toDomainObject(ProdutoInputDTO produtoInputDTO) {
    return modelMapper.map(produtoInputDTO, Produto.class);
  }

  public void copyToDomainObject(ProdutoInputDTO produtoInputDTO, Produto produto) {
    modelMapper.map(produtoInputDTO, produto);
  }
}
