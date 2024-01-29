package com.pedro.pagamento.api.controller.assembler;

import com.pedro.pagamento.dto.controller.ProdutoModelDTO;
import com.pedro.pagamento.model.Produto;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProdutoModelAssembler {
  @Autowired private ModelMapper modelMapper;

  public ProdutoModelDTO toModel(Produto produto) {
    return modelMapper.map(produto, ProdutoModelDTO.class);
  }

  public List<ProdutoModelDTO> toCollectionModel(List<Produto> produtos) {
    return produtos.stream().map(produto -> toModel(produto)).collect(Collectors.toList());
  }
}
