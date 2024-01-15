package com.pedro.pagamento.service;

import com.pedro.pagamento.adapter.MercadoPagoAdapter;
import com.pedro.pagamento.dto.response.MercadoPagoDto;
import com.pedro.pagamento.exception.ProdutoNaoEncontradoException;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Pagamento;
import com.pedro.pagamento.model.Produto;
import com.pedro.pagamento.repository.ClienteRepository;
import com.pedro.pagamento.repository.PagamentoRepository;
import com.pedro.pagamento.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GerenciadorDePagamentoMercadoPagoService {
  @Autowired private MercadoPagoAdapter formaDePagamentoAdapter;
  @Autowired private ClienteRepository clienteRepository;
  @Autowired private ProdutoRepository produtoRepository;
  @Autowired private CadastroPagamentoService cadastroPagamentoService;

  public MercadoPagoDto processaPagamento(Long id) {
    Cliente cliente =
        clienteRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

    Long produtoId = cliente.getProduto().getId();
    Produto produto =
        produtoRepository
            .findById(produtoId)
            .orElseThrow(() -> new ProdutoNaoEncontradoException(" Produto não encontrado"));
    MercadoPagoDto mercadoPagoDto = formaDePagamentoAdapter.realizaPagamento(cliente, produto);

    criaPagamentoTable(mercadoPagoDto, cliente);
    return mercadoPagoDto;
  }

  private void criaPagamentoTable(MercadoPagoDto mercadoPagoDto, Cliente cliente) {

    if (mercadoPagoDto != null && cliente != null) {
      cadastroPagamentoService.save(mercadoPagoDto, cliente);
    }
  }
}
