package com.pedro.pagamento.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pagamento {

  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;

  private String codigo;
  private Long idPagamento;
  private String status;

  @JsonIgnore
  @OneToOne
  @JoinColumn(name = "fk_cliente_id", nullable = false)
  private Cliente cliente;

  @PrePersist
  private void gerarCodigo() {
    setCodigo(UUID.randomUUID().toString());
  }
}
