package com.faculdade.forcavendas.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CondicaoPagamento { A_VISTA, A_PRAZO }

@Entity(tableName = "pedido_venda")
data class PedidoVenda(
    @PrimaryKey(autoGenerate = true)
    val codigo: Long = 0,
    val clienteCodigo: Long,
    val enderecoEntregaCodigo: Long,
    val condicaoPagamento: CondicaoPagamento,
    val numeroParcelas: Int,
    val quantidadeItens: Int,
    val valorTotalItens: Double,
    val valorFrete: Double,
    val valorTotal: Double,
    val dataPedido: Long
)
