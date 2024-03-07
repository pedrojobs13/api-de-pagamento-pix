package com.pedro.pagamento.webhookstate;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SendMessageState {
  @Value("${webhookFrontSend}")
  private String webhook;

  public void sendMessageToConsumer() {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = createHttpHeadres();

    ResponseEntity<String> response = restTemplate.getForEntity(webhook, String.class);

    System.out.println(response.getBody());
  }

  private HttpHeaders createHttpHeadres() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return headers;
  }
}
