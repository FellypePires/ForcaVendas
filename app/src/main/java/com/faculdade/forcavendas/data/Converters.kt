package com.faculdade.forcavendas.data

import androidx.room.TypeConverter
import com.faculdade.forcavendas.data.entity.CondicaoPagamento

class Converters {
    @TypeConverter
    fun fromCondicao(valor: CondicaoPagamento): String = valor.name

    @TypeConverter
    fun toCondicao(valor: String): CondicaoPagamento = CondicaoPagamento.valueOf(valor)
}
