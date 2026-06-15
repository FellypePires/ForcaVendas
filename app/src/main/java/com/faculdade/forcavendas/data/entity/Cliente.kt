package com.faculdade.forcavendas.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cliente",
    foreignKeys = [
        ForeignKey(
            entity = Endereco::class,
            parentColumns = ["codigo"],
            childColumns = ["codigoEndereco"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("codigoEndereco")]
)
data class Cliente(
    @PrimaryKey(autoGenerate = true)
    val codigo: Long = 0,
    val nome: String,
    val cpf: String,
    val dataNasc: String,
    val codigoEndereco: Long
)
