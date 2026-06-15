package com.faculdade.forcavendas.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "endereco")
data class Endereco(
    @PrimaryKey(autoGenerate = true)
    val codigo: Long = 0,
    val logradouro: String,
    val numero: String,
    val bairro: String,
    val cidade: String,
    val uf: String
) {
    fun resumo(): String = "$logradouro, $numero - $bairro, $cidade/$uf"
}
