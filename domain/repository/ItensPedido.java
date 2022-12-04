package io.github.lisfribeiro.Projeto.domain.repository;

import io.github.lisfribeiro.Projeto.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensPedido extends JpaRepository<ItemPedido, Integer> {
}
