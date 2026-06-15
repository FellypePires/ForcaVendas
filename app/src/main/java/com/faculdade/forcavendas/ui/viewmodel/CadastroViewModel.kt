package com.faculdade.forcavendas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faculdade.forcavendas.data.Repository
import com.faculdade.forcavendas.data.entity.Cliente
import com.faculdade.forcavendas.data.entity.Endereco
import com.faculdade.forcavendas.data.entity.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CadastroViewModel(private val repository: Repository) : ViewModel() {

    val enderecos: StateFlow<List<Endereco>> =
        repository.enderecos.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val clientes: StateFlow<List<Cliente>> =
        repository.clientes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val itens: StateFlow<List<Item>> =
        repository.itens.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensagem = MutableStateFlow<String?>(null)
    val mensagem: StateFlow<String?> = _mensagem
    fun limparMensagem() { _mensagem.value = null }
    private fun avisar(texto: String) { _mensagem.value = texto }
    fun salvarEndereco(
        logradouro: String,
        numero: String,
        bairro: String,
        cidade: String,
        uf: String,
        aoConcluir: () -> Unit
    ) {
        if (logradouro.isBlank() || numero.isBlank() || bairro.isBlank() ||
            cidade.isBlank() || uf.isBlank()
        ) {
            avisar("Preencha todos os campos do endereco.")
            return
        }
        if (uf.trim().length != 2) {
            avisar("UF deve ter 2 letras (ex.: PR).")
            return
        }
        viewModelScope.launch {
            repository.salvarEndereco(
                Endereco(
                    logradouro = logradouro.trim(),
                    numero = numero.trim(),
                    bairro = bairro.trim(),
                    cidade = cidade.trim(),
                    uf = uf.trim().uppercase()
                )
            )
            avisar("Endereco cadastrado com sucesso!")
            aoConcluir()
        }
    }
    fun salvarCliente(
        nome: String,
        cpf: String,
        dataNasc: String,
        codigoEndereco: Long?,
        aoConcluir: () -> Unit
    ) {
        if (nome.isBlank() || cpf.isBlank() || dataNasc.isBlank()) {
            avisar("Preencha nome, CPF e data de nascimento.")
            return
        }
        if (codigoEndereco == null) {
            avisar("Selecione um endereco para o cliente.")
            return
        }
        if (cpf.filter { it.isDigit() }.length != 11) {
            avisar("CPF deve conter 11 digitos.")
            return
        }
        viewModelScope.launch {
            repository.salvarCliente(
                Cliente(
                    nome = nome.trim(),
                    cpf = cpf.trim(),
                    dataNasc = dataNasc.trim(),
                    codigoEndereco = codigoEndereco
                )
            )
            avisar("Cliente cadastrado com sucesso!")
            aoConcluir()
        }
    }
    fun salvarItem(
        descricao: String,
        valorUnitTexto: String,
        unidadeMedida: String,
        aoConcluir: () -> Unit
    ) {
        if (descricao.isBlank() || valorUnitTexto.isBlank() || unidadeMedida.isBlank()) {
            avisar("Preencha todos os campos do item.")
            return
        }
        val valor = valorUnitTexto.replace(",", ".").toDoubleOrNull()
        if (valor == null) {
            avisar("Valor unitario invalido.")
            return
        }
        if (valor <= 0.0) {
            avisar("O valor unitario deve ser maior que zero.")
            return
        }
        viewModelScope.launch {
            repository.salvarItem(
                Item(
                    descricao = descricao.trim(),
                    valorUnit = valor,
                    unidadeMedida = unidadeMedida.trim()
                )
            )
            avisar("Item cadastrado com sucesso!")
            aoConcluir()
        }
    }
}
