package com.faculdade.forcavendas.data

import com.faculdade.forcavendas.data.entity.Cliente
import com.faculdade.forcavendas.data.entity.Endereco
import com.faculdade.forcavendas.data.entity.Item
import com.faculdade.forcavendas.data.entity.PedidoItem
import com.faculdade.forcavendas.data.entity.PedidoVenda
import com.faculdade.forcavendas.data.relation.PedidoComItens
import kotlinx.coroutines.flow.Flow

class Repository(private val db: AppDatabase) {
    val enderecos: Flow<List<Endereco>> = db.enderecoDao().listarTodos()
    suspend fun salvarEndereco(e: Endereco): Long = db.enderecoDao().inserir(e)
    suspend fun buscarEndereco(codigo: Long): Endereco? = db.enderecoDao().buscarPorCodigo(codigo)
    val clientes: Flow<List<Cliente>> = db.clienteDao().listarTodos()
    suspend fun salvarCliente(c: Cliente): Long = db.clienteDao().inserir(c)
    suspend fun buscarCliente(codigo: Long): Cliente? = db.clienteDao().buscarPorCodigo(codigo)
    val itens: Flow<List<Item>> = db.itemDao().listarTodos()
    suspend fun salvarItem(i: Item): Long = db.itemDao().inserir(i)
    val pedidos: Flow<List<PedidoComItens>> = db.pedidoDao().listarTodos()
    suspend fun salvarPedido(pedido: PedidoVenda, itens: List<PedidoItem>): Long =
        db.pedidoDao().salvarPedidoCompleto(pedido, itens)
    suspend fun buscarPedido(codigo: Long): PedidoComItens? = db.pedidoDao().buscarPorCodigo(codigo)
}
