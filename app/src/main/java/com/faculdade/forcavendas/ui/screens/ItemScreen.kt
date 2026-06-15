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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faculdade.forcavendas.ui.components.SecaoCard
import com.faculdade.forcavendas.ui.components.formatarMoeda
import com.faculdade.forcavendas.ui.viewmodel.CadastroViewModel

@Composable
fun ItemScreen(viewModel: CadastroViewModel) {
    val context = LocalContext.current
    val itens by viewModel.itens.collectAsStateWithLifecycle()
    val mensagem by viewModel.mensagem.collectAsStateWithLifecycle()

    LaunchedEffect(mensagem) {
        mensagem?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.limparMensagem()
        }
    }

    var descricao by rememberSaveable { mutableStateOf("") }
    var valorUnit by rememberSaveable { mutableStateOf("") }
    var unidade by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SecaoCard(titulo = "Cadastro de Item") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descricao") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = valorUnit,
                        onValueChange = { valorUnit = it },
                        label = { Text("Valor unitario") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = unidade,
                        onValueChange = { unidade = it },
                        label = { Text("Unidade (UN, KG...)") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Button(
                    onClick = {
                        viewModel.salvarItem(descricao, valorUnit, unidade) {
                            descricao = ""; valorUnit = ""; unidade = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Salvar item") }
            }
        }

        Text(
            "Itens cadastrados (${itens.size})",
            style = MaterialTheme.typography.titleMedium
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(itens) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Codigo ${item.codigo}", style = MaterialTheme.typography.labelMedium)
                        Text(item.descricao, style = MaterialTheme.typography.bodyLarge)
                        Text("${formatarMoeda(item.valorUnit)} / ${item.unidadeMedida}",
                            style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
