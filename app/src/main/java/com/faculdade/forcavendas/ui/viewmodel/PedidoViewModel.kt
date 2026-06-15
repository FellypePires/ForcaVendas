package com.faculdade.forcavendas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faculdade.forcavendas.data.CalculoPedido
import com.faculdade.forcavendas.data.Repository
import com.faculdade.forcavendas.data.entity.Cliente
import com.faculdade.forcavendas.data.entity.CondicaoPagamento
import com.faculdade.forcavendas.data.entity.Endereco
import com.faculdade.forcavendas.data.entity.Item
import com.faculdade.forcavendas.data.entity.PedidoItem
import com.faculdade.forcavendas.data.entity.PedidoVenda
import com.faculdade.forcavendas.data.relation.PedidoComItens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LinhaPedido(
    val item: Item,
    val quantidade: Double
) {
    val subtotal: Double get() = item.valorUnit * quantidade
}

data class PedidoUiState(
    val clienteSelecionado: Cliente? = null,
    val enderecoEntrega: Endereco? = null,
    val condicao: CondicaoPagamento = CondicaoPagamento.A_VISTA,
    val numeroParcelas: Int = 2,
    val linhas: List<LinhaPedido> = emptyList(),
    val consulta: PedidoComItens? = null
) {
    val quantidadeItens: Int get() = linhas.size
    val totalItens: Double get() = linhas.sumOf { it.subtotal }
    val frete: Double get() = enderecoEntrega?.let { CalculoPedido.calcularFrete(it) } ?: 0.0
    val total: Double get() = CalculoPedido.calcularTotal(totalItens, condicao, frete)
    val parcelas: List<Double>
        get() = if (condicao == CondicaoPagamento.A_PRAZO)
            CalculoPedido.calcularParcelas(total, numeroParcelas) else emptyList()
}

class PedidoViewModel(private val repository: Repository) : ViewModel() {

    val clientes: StateFlow<List<Cliente>> =
        repository.clientes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val enderecos: StateFlow<List<Endereco>> =
        repository.enderecos.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val itens: StateFlow<List<Item>> =
        repository.itens.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val pedidos: StateFlow<List<PedidoComItens>> =
        repository.pedidos.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _ui = MutableStateFlow(PedidoUiState())
    val ui: StateFlow<PedidoUiState> = _ui

    private val _mensagem = MutableStateFlow<String?>(null)
    val mensagem: StateFlow<String?> = _mensagem
    fun limparMensagem() { _mensagem.value = null }
    private fun avisar(texto: String) { _mensagem.value = texto }
    fun selecionarCliente(cliente: Cliente) {
        viewModelScope.launch {
            val enderecoCliente = repository.buscarEndereco(cliente.codigoEndereco)
            _ui.value = _ui.value.copy(
                clienteSelecionado = cliente,
                enderecoEntrega = enderecoCliente
            )
        }
    }

    fun selecionarEnderecoEntrega(endereco: Endereco) {
        _ui.value = _ui.value.copy(enderecoEntrega = endereco)
    }

    fun definirCondicao(condicao: CondicaoPagamento) {
        _ui.value = _ui.value.copy(condicao = condicao)
    }

    fun definirParcelas(quantidade: Int) {
        _ui.value = _ui.value.copy(numeroParcelas = quantidade)
    }
    fun adicionarItem(item: Item?, quantidadeTexto: String): Boolean {
        if (item == null) {
            avisar("Selecione um item para adicionar.")
            return false
        }
        val quantidade = quantidadeTexto.replace(",", ".").toDoubleOrNull()
        if (quantidade == null) {
            avisar("Quantidade invalida.")
            return false
        }
        if (quantidade <= 0.0) {
            avisar("A quantidade deve ser maior que zero.")
            return false
        }
        val linhas = _ui.value.linhas.toMutableList()
        val indice = linhas.indexOfFirst { it.item.codigo == item.codigo }
        if (indice >= 0) {
            val atual = linhas[indice]
            linhas[indice] = atual.copy(quantidade = atual.quantidade + quantidade)
        } else {
            linhas.add(LinhaPedido(item, quantidade))
        }
        _ui.value = _ui.value.copy(linhas = linhas)
        return true
    }

    fun removerLinha(linha: LinhaPedido) {
        _ui.value = _ui.value.copy(linhas = _ui.value.linhas - linha)
    }
    fun concluirPedido(aoConcluir: (Long) -> Unit) {
        val estado = _ui.value
        if (estado.clienteSelecionado == null) {
            avisar("Selecione o cliente do pedido.")
            return
        }
        if (estado.linhas.isEmpty()) {
            avisar("Adicione ao menos um item ao pedido.")
            return
        }
        if (estado.enderecoEntrega == null) {
            avisar("Selecione o endereco de entrega.")
            return
        }
        if (estado.condicao == CondicaoPagamento.A_PRAZO && estado.numeroParcelas < 1) {
            avisar("Informe a quantidade de parcelas.")
            return
        }

        viewModelScope.launch {
            val pedido = PedidoVenda(
                clienteCodigo = estado.clienteSelecionado.codigo,
                enderecoEntregaCodigo = estado.enderecoEntrega.codigo,
                condicaoPagamento = estado.condicao,
                numeroParcelas = if (estado.condicao == CondicaoPagamento.A_PRAZO) estado.numeroParcelas else 1,
                quantidadeItens = estado.quantidadeItens,
                valorTotalItens = estado.totalItens,
                valorFrete = estado.frete,
                valorTotal = estado.total,
                dataPedido = System.currentTimeMillis()
            )
            val itensPedido = estado.linhas.map { linha ->
                PedidoItem(
                    pedidoCodigo = 0,
                    itemCodigo = linha.item.codigo,
                    descricao = linha.item.descricao,
                    unidadeMedida = linha.item.unidadeMedida,
                    quantidade = linha.quantidade,
                    valorUnitario = linha.item.valorUnit,
                    subtotal = linha.subtotal
                )
            }
            val codigo = repository.salvarPedido(pedido, itensPedido)
            avisar("Pedido no $codigo cadastrado com sucesso!")
            limparTudo()
            aoConcluir(codigo)
        }
    }

    fun limparTudo() {
        _ui.value = PedidoUiState()
    }
    fun consultarPedido(codigoTexto: String) {
        val codigo = codigoTexto.toLongOrNull()
        if (codigo == null) {
            avisar("Informe um codigo de pedido valido.")
            return
        }
        viewModelScope.launch {
            val encontrado = repository.buscarPedido(codigo)
            if (encontrado == null) {
                avisar("Pedido no $codigo nao encontrado.")
                _ui.value = _ui.value.copy(consulta = null)
            } else {
                _ui.value = _ui.value.copy(consulta = encontrado)
            }
        }
    }

    fun fecharConsulta() {
        _ui.value = _ui.value.copy(consulta = null)
    }
}
