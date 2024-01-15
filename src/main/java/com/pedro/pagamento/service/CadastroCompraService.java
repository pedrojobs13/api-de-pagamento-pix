package com.pedro.pagamento.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.pagamento.model.Pagamento;
import com.pedro.pagamento.repository.PagamentoRepository;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class CadastroCompraService {
  @Autowired private PagamentoRepository pagamentoRepository;
  private final Map<String, Object> sanitize = new HashMap<>();

  @Value("${tokenMercadoPago}")
  private String token;

  public Pagamento verificarStatusCompra(Long id) {
    Pagamento pagamentoAtual = pagamentoRepository.findById(id).orElseThrow(RuntimeException::new);
    Long idEncontrado = pagamentoAtual.getId();

    Map<String, Object> response = getResponse(idEncontrado);

    return atualizarPagamento(response, pagamentoAtual);
  }

  private Pagamento atualizarPagamento(Map<String, Object> response, Pagamento pagamentoAtual) {

    if (response != null && pagamentoAtual.getStatus().equalsIgnoreCase("pending")) {
      merge(response, pagamentoAtual);
      BeanUtils.copyProperties(response, pagamentoAtual, "id");
      return pagamentoRepository.save(pagamentoAtual);
    }

    return pagamentoAtual;
  }

  private Map<String, Object> getResponse(Long idEncontrado) {
    ParameterizedTypeReference<Map<String, Object>> responseType =
        new ParameterizedTypeReference<>() {};

    String createPaymentUri = "https://api.mercadopago.com/v1/payments/" + idEncontrado;
    HttpHeaders headers = createHttpHeadres();

    RestTemplate restTemplate = new RestTemplate();

    HttpEntity<String> entity = new HttpEntity<>(headers);

    Map<String, Object> response =
        restTemplate.exchange(createPaymentUri, HttpMethod.GET, entity, responseType).getBody();

    assert response != null;
    sanitize.put("id", response.get("id"));
    sanitize.put("status", response.get("status"));
    return sanitize;
  }

  private void merge(Map<String, Object> dadosOrigem, Pagamento pagamentoDestino) {
    Objects.requireNonNull(dadosOrigem);
    ObjectMapper mapper = new ObjectMapper();
    Pagamento pagamentoOrigem = mapper.convertValue(dadosOrigem, Pagamento.class);

    dadosOrigem.forEach(
        (key, value) -> {
          Field field = ReflectionUtils.findField(Pagamento.class, key);
          field.setAccessible(true); // libera os atributos privados
          Object novoValor = ReflectionUtils.getField(field, pagamentoOrigem);
          ReflectionUtils.setField(field, pagamentoDestino, novoValor);
        });
  }

  private HttpHeaders createHttpHeadres() {
    String autorizacao = token;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("Authorization", "Bearer " + autorizacao);
    return headers;
  }
}
