package io.github.lisfribeiro.Projeto.service.impl;

import io.github.lisfribeiro.Projeto.domain.entity.Cliente;
import io.github.lisfribeiro.Projeto.domain.entity.ItemPedido;
import io.github.lisfribeiro.Projeto.domain.entity.Pedido;
import io.github.lisfribeiro.Projeto.domain.entity.Produto;
import io.github.lisfribeiro.Projeto.domain.enums.StatusPedido;
import io.github.lisfribeiro.Projeto.domain.repository.Clientes;
import io.github.lisfribeiro.Projeto.domain.repository.ItensPedido;
import io.github.lisfribeiro.Projeto.domain.repository.Pedidos;
import io.github.lisfribeiro.Projeto.domain.repository.Produtos;
import io.github.lisfribeiro.Projeto.exception.PedidoNaoEncontradoException;
import io.github.lisfribeiro.Projeto.exception.RegraNegocioException;
import io.github.lisfribeiro.Projeto.rest.dto.ItemPedidoDTO;
import io.github.lisfribeiro.Projeto.rest.dto.PedidoDTO;
import io.github.lisfribeiro.Projeto.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServiceImpl implements PedidoService {
    private final  Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItensPedido itensPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar( PedidoDTO dto ) {
        Integer idCliente = dto.getCliente();
       Cliente cliente =  clientesRepository
               .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

       List<ItemPedido> itensPedido = converterItens(pedido, dto.getItens());
       repository.save(pedido);
       itensPedidoRepository.saveAll(itensPedido);
       pedido.setItens(itensPedido);
        return pedido;
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens) {
        if(itens.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens");
        }
        return itens
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                  Produto produto =  produtosRepository.findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException("Código de produto inválido" + idProduto
                                    ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository.
                findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }
}