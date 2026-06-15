package com.faculdade.forcavendas.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pedido_item",
    foreignKeys = [
        ForeignKey(
            entity = PedidoVenda::class,
            parentColumns = ["codigo"],
            childColumns = ["pedidoCodigo"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("pedidoCodigo")]
)
data class PedidoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pedidoCodigo: Long,
    val itemCodigo: Long,
    val descricao: String,
    val unidadeMedida: String,
    val quantidade: Double,
    val valorUnitario: Double,
    val subtotal: Double
)
