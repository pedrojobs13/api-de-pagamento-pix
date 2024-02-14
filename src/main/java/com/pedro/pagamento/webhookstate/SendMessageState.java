package com.pedro.pagamento.webhookstate;

import com.pedro.pagamento.dto.response.MessageToConsumerDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SendMessageState {
  public void sendMessageToConsumer() {
    RestTemplate restTemplate = new RestTemplate();
    return;
  }
}
