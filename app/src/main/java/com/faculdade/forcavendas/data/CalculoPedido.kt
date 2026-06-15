package com.faculdade.forcavendas.data

import com.faculdade.forcavendas.data.entity.CondicaoPagamento
import com.faculdade.forcavendas.data.entity.Endereco

object CalculoPedido {

    const val CIDADE_BASE = "Toledo"
    const val UF_BASE = "PR"

    const val PERCENTUAL = 0.05
    const val FRETE_OUTRA_CIDADE = 20.0
    const val FRETE_ADICIONAL_OUTRO_ESTADO = 50.0

    fun calcularFrete(endereco: Endereco): Double {
        val mesmaCidade = endereco.cidade.trim().equals(CIDADE_BASE, ignoreCase = true)
        val mesmoEstado = endereco.uf.trim().equals(UF_BASE, ignoreCase = true)

        if (mesmaCidade && mesmoEstado) return 0.0

        var frete = FRETE_OUTRA_CIDADE
        if (!mesmoEstado) frete += FRETE_ADICIONAL_OUTRO_ESTADO
        return frete
    }

    fun aplicarCondicao(totalItens: Double, condicao: CondicaoPagamento): Double =
        when (condicao) {
            CondicaoPagamento.A_VISTA -> totalItens * (1 - PERCENTUAL)
            CondicaoPagamento.A_PRAZO -> totalItens * (1 + PERCENTUAL)
        }

    fun calcularTotal(totalItens: Double, condicao: CondicaoPagamento, frete: Double): Double =
        aplicarCondicao(totalItens, condicao) + frete

    fun calcularParcelas(total: Double, numeroParcelas: Int): List<Double> {
        if (numeroParcelas <= 0) return emptyList()
        val totalCentavos = Math.round(total * 100)
        val base = totalCentavos / numeroParcelas
        val sobra = totalCentavos - (base * numeroParcelas)
        return (0 until numeroParcelas).map { indice ->
            val centavos = base + if (indice == numeroParcelas - 1) sobra else 0
            centavos / 100.0
        }
    }
}
