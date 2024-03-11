package com.pedro.pagamento.api.controller.assembler;

import com.pedro.pagamento.dto.controller.ClienteModelDTO;
import com.pedro.pagamento.dto.controller.PagamentoDTO;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Pagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PagamentoModelAssembler {
    @Autowired
    ModelMapper modelMapper;

    public PagamentoDTO toModel(Pagamento pagamento) {
        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public List<PagamentoDTO> toCollectionModel(List<Pagamento> pagamentos) {
        return pagamentos.stream().map(pagamento -> toModel(pagamento)).collect(Collectors.toList());
    }
}
