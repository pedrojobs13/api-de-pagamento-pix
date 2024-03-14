package com.pedro.pagamento.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.pagamento.adapter.MercadoPagoAdapter;
import com.pedro.pagamento.dto.response.MercadoPagoDto;
import com.pedro.pagamento.dto.response.PayerPixDTO;
import com.pedro.pagamento.dto.response.PointOfInteractionDTO;
import com.pedro.pagamento.dto.response.TransactionDataDTO;
import com.pedro.pagamento.exception.BusinessException;
import com.pedro.pagamento.exception.ClienteNaoEncontradoException;
import com.pedro.pagamento.exception.ProdutoNaoEncontradoException;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Pagamento;
import com.pedro.pagamento.model.Produto;
import com.pedro.pagamento.repository.ClienteRepository;
import com.pedro.pagamento.repository.PagamentoRepository;
import com.pedro.pagamento.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class GerenciadorDePagamentoMercadoPagoService {
    @Autowired
    private MercadoPagoAdapter formaDePagamentoAdapter;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private CadastroPagamentoService cadastroPagamentoService;
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Value("${tokenMercadoPago}")
    private String token;

    private static final String MSG_CLIENTE_NAO_ENCONTRADO = "Cliente de id [%d] não foi encontrado";
    private static final String MSG_PRODUTO_NAO_ENCONTADO = "Produot de id [%d] não foi encontrado";

    @Transactional
    public MercadoPagoDto processaPagamento(String codigo) throws JsonProcessingException {
        Cliente cliente = buscaCliente(codigo);
        Long produtoId = cliente.getProduto().getId();

        Produto produto = buscaProduto(produtoId);

        if (cliente.getPagamento() == null) {

            MercadoPagoDto mercadoPagoDto = formaDePagamentoAdapter.realizaPagamento(cliente, produto);

            criaPagamentoTable(mercadoPagoDto, cliente);

            return mercadoPagoDto;
        } else {
            Long idDoPagamento = cliente.getPagamento().getIdPagamento();
            StringBuilder createPaymentUri = new StringBuilder();
            RestTemplate restTemplate = new RestTemplate();
            createPaymentUri.append("https://api.mercadopago.com/v1/payments/").append(idDoPagamento);
            HttpEntity request = new HttpEntity(headers());
            ResponseEntity<String> response = restTemplate.exchange(createPaymentUri.toString(), HttpMethod.GET, request, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            List<JsonNode> itens = StreamSupport.stream(root.path("additional_info")
                    .withArray("items").spliterator(), false).toList();


            JsonNode id = root.path("id");
            JsonNode status = root.path("status");
            JsonNode dataExpiracao = root.path("date_of_expiration");
            JsonNode totalApagar = root.path("transaction_amount");
            JsonNode qrCode = root.path("point_of_interaction").path("transaction_data").path("qr_code");

            JsonNode firstName = root.path("additional_info").path("payer").path("first_name");
            JsonNode lastName = root.path("additional_info").path("payer").path("last_name");

            TransactionDataDTO transactionDataDTO = TransactionDataDTO.builder()
                    .qrCode(qrCode.textValue())
                    .ticketUrl(qrCode.textValue())
                    .build();

            PointOfInteractionDTO pointOfInteractionDTO =
                    PointOfInteractionDTO.builder()
                            .transactionData(transactionDataDTO)
                            .build();

            PayerPixDTO payerPixDTO = PayerPixDTO.builder()
                    .FirstName(firstName.asText())
                    .LastName(lastName.asText()).build();

            MercadoPagoDto mercadoPagoDto = MercadoPagoDto
                    .builder()
                    .id(id.asLong())
                    .status(status.textValue())
                    .dataDeExpiracao(dataExpiracao.textValue())
                    .transactionAmount(totalApagar.decimalValue())
                    .pointOfInteraction(pointOfInteractionDTO)
                    .payerPixDTO(payerPixDTO)
                    .build();
            return mercadoPagoDto;
        }


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


    private HttpHeaders headers() {
        String autorizacao = token;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + autorizacao);
        return headers;
    }
}
