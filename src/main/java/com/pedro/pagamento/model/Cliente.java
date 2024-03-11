package com.pedro.pagamento.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

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
