package com.pedro.pagamento.service;

import com.pedro.pagamento.adapter.MercadoPagoAdapter;
import com.pedro.pagamento.dto.response.MercadoPagoDto;
import com.pedro.pagamento.exception.ClienteNaoEncontradoException;
import com.pedro.pagamento.exception.ProdutoNaoEncontradoException;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Pagamento;
import com.pedro.pagamento.model.Produto;
import com.pedro.pagamento.repository.ClienteRepository;
import com.pedro.pagamento.repository.PagamentoRepository;
import com.pedro.pagamento.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GerenciadorDePagamentoMercadoPagoService {
  @Autowired private MercadoPagoAdapter formaDePagamentoAdapter;
  @Autowired private ClienteRepository clienteRepository;
  @Autowired private ProdutoRepository produtoRepository;
  @Autowired private CadastroPagamentoService cadastroPagamentoService;
  private static final String MSG_CLIENTE_NAO_ENCONTRADO = "Cliente de id [%d] não foi encontrado";
  private static final String MSG_PRODUTO_NAO_ENCONTADO = "Produot de id [%d] não foi encontrado";

  @Transactional
  public MercadoPagoDto processaPagamento(String codigo) {
    Cliente cliente = buscaCliente(codigo);
    Long produtoId = cliente.getProduto().getId();

    Produto produto = buscaProduto(produtoId);

    MercadoPagoDto mercadoPagoDto = formaDePagamentoAdapter.realizaPagamento(cliente, produto);

    criaPagamentoTable(mercadoPagoDto, cliente);

    return mercadoPagoDto;
  }

  private void criaPagamentoTable(MercadoPagoDto mercadoPagoDto, Cliente cliente) {

    if (mercadoPagoDto != null && cliente != null) {
      cadastroPagamentoService.save(mercadoPagoDto, cliente);
    }
  }

  private Cliente buscaCliente(String codigo) {
    return clienteRepository
        .findByCodigo(codigo)
        .orElseThrow(() -> new ClienteNaoEncontradoException(MSG_CLIENTE_NAO_ENCONTRADO));
  }

  private Produto buscaProduto(Long produtoId) {
    return produtoRepository
        .findById(produtoId)
        .orElseThrow(() -> new ProdutoNaoEncontradoException(MSG_PRODUTO_NAO_ENCONTADO));
  }
}
