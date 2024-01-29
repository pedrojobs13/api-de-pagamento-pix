package com.pedro.pagamento.repository;

import com.pedro.pagamento.model.Cliente;
import com.pedro.pagamento.model.Pagamento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
  Optional<Cliente> findByCodigo(String codigo);
}
