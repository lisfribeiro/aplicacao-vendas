package io.github.lisfribeiro.Projeto.domain.repository;

import io.github.lisfribeiro.Projeto.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {
}
