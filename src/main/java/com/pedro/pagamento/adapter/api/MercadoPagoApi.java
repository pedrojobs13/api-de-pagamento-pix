package com.pedro.pagamento.adapter.api;

import com.pedro.pagamento.dto.mercadoPago.AdditionalInfoDTO;
import com.pedro.pagamento.dto.mercadoPago.AdditionalPayer;
import com.pedro.pagamento.dto.mercadoPago.IdentificationDTO;
import com.pedro.pagamento.dto.mercadoPago.ItemDTO;
import com.pedro.pagamento.dto.mercadoPago.PayerDTO;
import com.pedro.pagamento.dto.mercadoPago.PaymentDTO;
import com.pedro.pagamento.dto.response.MercadoPagoDto;
import com.pedro.pagamento.dto.response.PayerPixDTO;
import com.pedro.pagamento.dto.response.PointOfInteractionDTO;
import com.pedro.pagamento.dto.response.TransactionDataDTO;
import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Produto;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MercadoPagoApi {
  @Autowired private PaymentDTO paymentDTO;
  @Autowired private IdentificationDTO identificationDTO;
  @Autowired private PayerDTO payerDTO;
  @Autowired private AdditionalInfoDTO additionalInfoDTO;
  @Autowired private ItemDTO itemDTO;
  @Autowired private AdditionalPayer additionalPayer;

  @Value("${tokenMercadoPago}")
  private String token;

  public MercadoPagoDto payment(Cliente cliente, Produto produto) {
    Objects.requireNonNull(cliente);
    Objects.requireNonNull(produto);

    dtoJson(cliente, produto);

    try {
      String createPaymentUri = "https://api.mercadopago.com/v1/payments";
      HttpHeaders headers = createHttpHeadres();

      RestTemplate restTemplate = new RestTemplate();

      HttpEntity<PaymentDTO> request = new HttpEntity<>(paymentDTO, headers);

      var response =
          restTemplate.exchange(createPaymentUri, HttpMethod.POST, request, String.class).getBody();

      MercadoPagoDto mercadoPagoDto = null;

      if (response != null) {
        mercadoPagoDto = getMercadoPagoDtoResponse(response);
      }

      return mercadoPagoDto;

    } catch (ClassCastException e) {
      throw new IllegalArgumentException(e.getCause());
    }
  }

  private static MercadoPagoDto getMercadoPagoDtoResponse(String response) {
    MercadoPagoDto mercadoPagoDto;
    JsonReader jsonReader = Json.createReader(new StringReader(response));
    JsonObject jsonObject = jsonReader.readObject();

    String status = jsonObject.getString("status");
    Long id = jsonObject.getJsonNumber("id").longValueExact();
    String dataDeExpiracao = jsonObject.getString("date_of_expiration");
    BigDecimal transactionAmount = jsonObject.getJsonNumber("transaction_amount").bigDecimalValue();

    JsonObject pointOfInteraction = jsonObject.getJsonObject("point_of_interaction");
    JsonObject transactionData = pointOfInteraction.getJsonObject("transaction_data");
    String qrCode = transactionData.getString("qr_code");
    String ticketUrl = transactionData.getString("ticket_url");

    JsonObject additionalInfo = jsonObject.getJsonObject("additional_info");
    JsonObject payer = additionalInfo.getJsonObject("payer");
    String nome = payer.getString("first_name");
    String sobrenome = payer.getString("last_name");

    TransactionDataDTO transactionDataDTO =
        TransactionDataDTO.builder().qrCode(qrCode).ticketUrl(ticketUrl).build();

    PointOfInteractionDTO pointOfInteractionDTO =
        PointOfInteractionDTO.builder().transactionData(transactionDataDTO).build();

    PayerPixDTO payerPixDTO = PayerPixDTO.builder().FirstName(nome).LastName(sobrenome).build();

    mercadoPagoDto =
        MercadoPagoDto.builder()
            .status(status)
            .id(id)
            .dataDeExpiracao(dataDeExpiracao)
            .transactionAmount(transactionAmount)
            .pointOfInteraction(pointOfInteractionDTO)
            .payerPixDTO(payerPixDTO)
            .build();
    return mercadoPagoDto;
  }

  private void dtoJson(Cliente cliente, Produto produto) {
    payerDTO.setEmail(cliente.getEmail());
    payerDTO.setFirst_name(cliente.getNome());
    payerDTO.setLast_name(cliente.getSobrenome());

    identificationDTO.setType("CPF");
    identificationDTO.setNumber("21750314037");
    payerDTO.setIdentification(identificationDTO);

    itemDTO.setId(produto.getId());
    itemDTO.setTitle(produto.getTitle());
    itemDTO.setDescription(produto.getDescricao());
    itemDTO.setPicture_url(produto.getFoto());
    itemDTO.setUnit_price(produto.getValor());

    additionalPayer.setFirst_name(cliente.getNome());
    additionalPayer.setLast_name(cliente.getSobrenome());

    additionalInfoDTO.setItems(List.of(itemDTO));
    additionalInfoDTO.setPayer(additionalPayer);

    paymentDTO.setPayment_method_id("pix");
    paymentDTO.setDescription(produto.getDescricao());
    paymentDTO.setTransaction_amount(cliente.getValor());
    paymentDTO.setPayer(payerDTO);
    paymentDTO.setAdditional_info(additionalInfoDTO);
  }

  private HttpHeaders createHttpHeadres() {

    String autorizacao = token;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("Authorization", "Bearer " + autorizacao);
    headers.add("X-Idempotency-Key", UUID.randomUUID().toString());

    return headers;
  }
}
