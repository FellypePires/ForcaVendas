package com.faculdade.forcavendas.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val codigo: Long = 0,
    val descricao: String,
    val valorUnit: Double,
    val unidadeMedida: String
)
