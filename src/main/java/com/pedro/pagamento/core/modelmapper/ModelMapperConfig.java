package com.pedro.pagamento.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
  private ModelMapper modelMapper;

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
