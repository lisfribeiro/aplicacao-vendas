package io.github.lisfribeiro.Projeto.service;

import io.github.lisfribeiro.Projeto.domain.entity.Pedido;
import io.github.lisfribeiro.Projeto.domain.enums.StatusPedido;
import io.github.lisfribeiro.Projeto.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto (Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
