package com.faculdade.forcavendas.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.faculdade.forcavendas.data.entity.PedidoItem
import com.faculdade.forcavendas.data.entity.PedidoVenda
import com.faculdade.forcavendas.data.relation.PedidoComItens
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {

    @Insert
    suspend fun inserirPedido(pedido: PedidoVenda): Long

    @Insert
    suspend fun inserirItens(itens: List<PedidoItem>)

    @Transaction
    suspend fun salvarPedidoCompleto(pedido: PedidoVenda, itens: List<PedidoItem>): Long {
        val codigo = inserirPedido(pedido)
        inserirItens(itens.map { it.copy(pedidoCodigo = codigo) })
        return codigo
    }

    @Transaction
    @Query("SELECT * FROM pedido_venda WHERE codigo = :codigo")
    suspend fun buscarPorCodigo(codigo: Long): PedidoComItens?

    @Transaction
    @Query("SELECT * FROM pedido_venda ORDER BY codigo DESC")
    fun listarTodos(): Flow<List<PedidoComItens>>
}
