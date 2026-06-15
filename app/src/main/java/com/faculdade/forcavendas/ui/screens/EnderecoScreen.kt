package com.faculdade.forcavendas.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faculdade.forcavendas.ui.components.SecaoCard
import com.faculdade.forcavendas.ui.viewmodel.CadastroViewModel

@Composable
fun EnderecoScreen(viewModel: CadastroViewModel) {
    val context = LocalContext.current
    val enderecos by viewModel.enderecos.collectAsStateWithLifecycle()
    val mensagem by viewModel.mensagem.collectAsStateWithLifecycle()

    LaunchedEffect(mensagem) {
        mensagem?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.limparMensagem()
        }
    }

    var logradouro by rememberSaveable { mutableStateOf("") }
    var numero by rememberSaveable { mutableStateOf("") }
    var bairro by rememberSaveable { mutableStateOf("") }
    var cidade by rememberSaveable { mutableStateOf("") }
    var uf by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SecaoCard(titulo = "Cadastro de Endereco") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = logradouro,
                    onValueChange = { logradouro = it },
                    label = { Text("Logradouro") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = numero,
                        onValueChange = { numero = it },
                        label = { Text("Numero") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = bairro,
                        onValueChange = { bairro = it },
                        label = { Text("Bairro") },
                        modifier = Modifier.weight(2f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = cidade,
                        onValueChange = { cidade = it },
                        label = { Text("Cidade") },
                        modifier = Modifier.weight(2f)
                    )
                    OutlinedTextField(
                        value = uf,
                        onValueChange = { if (it.length <= 2) uf = it.uppercase() },
                        label = { Text("UF") },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                        modifier = Modifier.weight(1f)
                    )
                }
                Button(
                    onClick = {
                        viewModel.salvarEndereco(logradouro, numero, bairro, cidade, uf) {
                            logradouro = ""; numero = ""; bairro = ""; cidade = ""; uf = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Salvar endereco") }
            }
        }

        Text(
            "Enderecos cadastrados (${enderecos.size})",
            style = MaterialTheme.typography.titleMedium
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(enderecos) { endereco ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Codigo ${endereco.codigo}", style = MaterialTheme.typography.labelMedium)
                        Text(endereco.resumo(), style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
