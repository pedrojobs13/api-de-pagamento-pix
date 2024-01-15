package com.pedro.pagamento.repository;

import com.pedro.pagamento.model.Pagamento;
import com.pedro.pagamento.model.Produto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {}
