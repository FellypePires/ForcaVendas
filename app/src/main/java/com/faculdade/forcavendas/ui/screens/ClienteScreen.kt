package com.faculdade.forcavendas.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faculdade.forcavendas.data.entity.Endereco
import com.faculdade.forcavendas.ui.components.AppDropdown
import com.faculdade.forcavendas.ui.components.SecaoCard
import com.faculdade.forcavendas.ui.viewmodel.CadastroViewModel

@Composable
fun ClienteScreen(viewModel: CadastroViewModel) {
    val context = LocalContext.current
    val clientes by viewModel.clientes.collectAsStateWithLifecycle()
    val enderecos by viewModel.enderecos.collectAsStateWithLifecycle()
    val mensagem by viewModel.mensagem.collectAsStateWithLifecycle()

    LaunchedEffect(mensagem) {
        mensagem?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.limparMensagem()
        }
    }

    var nome by rememberSaveable { mutableStateOf("") }
    var cpf by rememberSaveable { mutableStateOf("") }
    var dataNasc by rememberSaveable { mutableStateOf("") }
    var enderecoSelecionado by remember { mutableStateOf<Endereco?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SecaoCard(titulo = "Cadastro de Cliente") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cpf,
                    onValueChange = { if (it.length <= 14) cpf = it },
                    label = { Text("CPF (somente numeros)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dataNasc,
                    onValueChange = { dataNasc = it },
                    label = { Text("Data de nascimento (dd/MM/aaaa)") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (enderecos.isEmpty()) {
                    Text(
                        "Cadastre um endereco antes de criar o cliente.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                AppDropdown(
                    rotulo = "Endereco do cliente",
                    opcoes = enderecos,
                    selecionado = enderecoSelecionado,
                    textoDe = { "Cod ${it.codigo} - ${it.resumo()}" },
                    aoSelecionar = { enderecoSelecionado = it }
                )
                Button(
                    onClick = {
                        viewModel.salvarCliente(nome, cpf, dataNasc, enderecoSelecionado?.codigo) {
                            nome = ""; cpf = ""; dataNasc = ""; enderecoSelecionado = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Salvar cliente") }
            }
        }

        Text(
            "Clientes cadastrados (${clientes.size})",
            style = MaterialTheme.typography.titleMedium
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(clientes) { cliente ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Codigo ${cliente.codigo}", style = MaterialTheme.typography.labelMedium)
                        Text(cliente.nome, style = MaterialTheme.typography.bodyLarge)
                        Text("CPF: ${cliente.cpf}  -  Nasc.: ${cliente.dataNasc}",
                            style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
