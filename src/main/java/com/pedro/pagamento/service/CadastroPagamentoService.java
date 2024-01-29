package com.pedro.pagamento.service;

import com.pedro.pagamento.dto.response.MercadoPagoDto;
import com.pedro.pagamento.exception.ClienteNaoEncontradoException;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Pagamento;
import com.pedro.pagamento.repository.ClienteRepository;
import com.pedro.pagamento.repository.PagamentoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroPagamentoService {
  @Autowired PagamentoRepository pagamentoRepository;
  @Autowired CadastroClienteService cadastroClienteService;
  @Autowired ClienteRepository clienteRepository;
  private final Pagamento pagamento = new Pagamento();

  @Transactional
  public void save(MercadoPagoDto mercadoPagoDto, Cliente cliente) {
    if (mercadoPagoDto != null && cliente != null) {
      String status = mercadoPagoDto.getStatus();
      Long idCompra = mercadoPagoDto.getId();

      pagamento.setStatus(status);
      pagamento.setIdPagamento(idCompra);

      pagamento.setCliente(cliente);
      cliente.setPagamento(pagamento);

      cadastroClienteService.inserir(cliente);
      pagamento.setCodigo(UUID.randomUUID().toString());
      pagamentoRepository.save(pagamento);
    } else {
      throw new IllegalArgumentException("MercadoPagoDto e Cliente devem ser fornecidos");
    }
  }

  public List<Cliente> getPagadores() {
    List<Long> clienteId = new ArrayList<>();

    for (Pagamento pagamento1 :
        pagamentoRepository.findAllByStatusContainingIgnoreCase("approved")) {
      clienteId.add(pagamento1.getCliente().getId());
    }
    if (!clienteId.isEmpty()) {
      return clienteRepository.findAllById(clienteId);
    }
    throw new ClienteNaoEncontradoException("nenhum cliente pagante até o momento");
  }
}
