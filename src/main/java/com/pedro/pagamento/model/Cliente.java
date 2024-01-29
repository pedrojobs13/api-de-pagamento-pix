package com.pedro.pagamento.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyGroup;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cliente {

  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;

  private String codigo;
  private String nome;
  private String sobrenome;
  private String email;
  private BigDecimal valor;

//  @JsonIgnoreProperties("hibernateLazyInitializer")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "produto_id", nullable = false)
  private Produto produto;


  @OneToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "fk_pagamento_id", nullable = true)
  private Pagamento pagamento;

  @PrePersist
  private void gerarCodigo() {
    setCodigo(UUID.randomUUID().toString());
  }
}
