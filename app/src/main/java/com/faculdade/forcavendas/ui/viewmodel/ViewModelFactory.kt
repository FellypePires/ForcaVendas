package com.faculdade.forcavendas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.faculdade.forcavendas.data.Repository

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CadastroViewModel::class.java) ->
                CadastroViewModel(repository) as T
            modelClass.isAssignableFrom(PedidoViewModel::class.java) ->
                PedidoViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
        }
    }
}
