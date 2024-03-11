package com.pedro.pagamento.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.pagamento.dto.response.WebhookDTO;
import com.pedro.pagamento.exception.BusinessException;
import com.pedro.pagamento.exception.PagamentoNaoEncontradoException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;

@Service
public class VerificaEAtualizaPagamentoService {
  @Autowired private PagamentoRepository pagamentoRepository;
  private final Map<String, Object> sanitize = new HashMap<>();
  private final ObjectMapper mapper = new ObjectMapper();
  private static final String MSG_PAGAMENTO_NAO_ENCONTRADO =
      "Pagamento de id [%d] não foi encontrado";

  @Value("${tokenMercadoPago}")
  private String token;

  @Transactional
  public Pagamento verificarEAtualizaStatusDaCompra(Long id) {
    Pagamento pagamentoAtual =
        pagamentoRepository
            .findFirstByIdPagamento(id)
            .orElseThrow(
                () ->
                    new PagamentoNaoEncontradoException(
                        String.format(MSG_PAGAMENTO_NAO_ENCONTRADO, id)));

    Long idEncontrado = pagamentoAtual.getIdPagamento();

    Map<String, Object> response = getResponse(idEncontrado);

    return atualizarPagamento(response, pagamentoAtual);
  }

  @Transactional
  public void recebeWebHookPagamento(String JsonresponseBody) {
    try {
      WebhookDTO webhookDTO = mapper.readValue(JsonresponseBody, WebhookDTO.class);
      System.out.println(webhookDTO);

      if (webhookDTO.getAction().equalsIgnoreCase("payment.updated")) {

        Long idWebhook = webhookDTO.getData().getId();

        Pagamento pagamentoAtual =
            pagamentoRepository
                .findFirstByIdPagamento(idWebhook)
                .orElseThrow(
                    () ->
                        new PagamentoNaoEncontradoException(
                            String.format(MSG_PAGAMENTO_NAO_ENCONTRADO, idWebhook)));

        System.out.println(pagamentoAtual.getIdPagamento());

        Map<String, Object> response = getResponse(webhookDTO.getData().getId());

        atualizarPagamento(response, pagamentoAtual);
      }

    } catch (JsonProcessingException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  private Pagamento atualizarPagamento(Map<String, Object> response, Pagamento pagamentoAtual) {

    if (response != null && pagamentoAtual.getStatus().equalsIgnoreCase("pending")) {
      merge(response, pagamentoAtual);
      BeanUtils.copyProperties(response, pagamentoAtual, "id", "idPagamento");
      return pagamentoRepository.save(pagamentoAtual);
    }

    return pagamentoAtual;
  }

  private Map<String, Object> getResponse(Long idEncontrado) {

    try {

      ParameterizedTypeReference<Map<String, Object>> responseType =
          new ParameterizedTypeReference<>() {};

      String createPaymentUri = "https://api.mercadopago.com/v1/payments/" + idEncontrado;

      HttpHeaders headers = createHttpHeadres();

      RestTemplate restTemplate = new RestTemplate();

      HttpEntity<String> entity = new HttpEntity<>(headers);

      Map<String, Object> response =
          restTemplate.exchange(createPaymentUri, HttpMethod.GET, entity, responseType).getBody();

      assert response != null;
      sanitize.put("idPagamento", response.get("id"));
      sanitize.put("status", response.get("status"));

      return sanitize;
    } catch (Unauthorized | NotFound e) {
      throw new BusinessException(e.getMessage());
    }
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
