package com.faculdade.forcavendas.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.faculdade.forcavendas.data.entity.PedidoItem
import com.faculdade.forcavendas.data.entity.PedidoVenda

data class PedidoComItens(
    @Embedded val pedido: PedidoVenda,
    @Relation(
        parentColumn = "codigo",
        entityColumn = "pedidoCodigo"
    )
    val itens: List<PedidoItem>
)
