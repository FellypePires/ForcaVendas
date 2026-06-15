package com.faculdade.forcavendas.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faculdade.forcavendas.data.entity.CondicaoPagamento
import com.faculdade.forcavendas.data.entity.Item
import com.faculdade.forcavendas.ui.components.AppDropdown
import com.faculdade.forcavendas.ui.components.SecaoCard
import com.faculdade.forcavendas.ui.components.formatarMoeda
import com.faculdade.forcavendas.ui.viewmodel.PedidoViewModel

@Composable
fun PedidoScreen(viewModel: PedidoViewModel) {
    val context = LocalContext.current
    val estado by viewModel.ui.collectAsStateWithLifecycle()
    val clientes by viewModel.clientes.collectAsStateWithLifecycle()
    val enderecos by viewModel.enderecos.collectAsStateWithLifecycle()
    val itens by viewModel.itens.collectAsStateWithLifecycle()
    val pedidos by viewModel.pedidos.collectAsStateWithLifecycle()
    val mensagem by viewModel.mensagem.collectAsStateWithLifecycle()

    LaunchedEffect(mensagem) {
        mensagem?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.limparMensagem()
        }
    }

    var itemSelecionado by remember { mutableStateOf<Item?>(null) }
    var quantidade by remember { mutableStateOf("") }
    var codigoConsulta by remember { mutableStateOf("") }
    var parcelasTexto by remember { mutableStateOf(estado.numeroParcelas.toString()) }

    fun limparCamposLocais() {
        itemSelecionado = null
        quantidade = ""
        codigoConsulta = ""
        parcelasTexto = "2"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SecaoCard(titulo = "Pedido de Venda") {
            Text(
                "Codigo do pedido: gerado automaticamente ao concluir.",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = codigoConsulta,
                    onValueChange = { codigoConsulta = it },
                    label = { Text("Consultar no do pedido") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = { viewModel.consultarPedido(codigoConsulta) }) {
                    Text("Buscar")
                }
            }
        }
        SecaoCard(titulo = "Cliente") {
            if (clientes.isEmpty()) {
                Text("Cadastre um cliente primeiro.", color = MaterialTheme.colorScheme.error)
            }
            AppDropdown(
                rotulo = "Selecione o cliente",
                opcoes = clientes,
                selecionado = estado.clienteSelecionado,
                textoDe = { "Cod ${it.codigo} - ${it.nome}" },
                aoSelecionar = { viewModel.selecionarCliente(it) },
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        SecaoCard(titulo = "Adicionar item") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                AppDropdown(
                    rotulo = "Item",
                    opcoes = itens,
                    selecionado = itemSelecionado,
                    textoDe = { it.descricao },
                    aoSelecionar = { itemSelecionado = it }
                )
                itemSelecionado?.let { item ->
                    Text(
                        "Valor unitario: ${formatarMoeda(item.valorUnit)}  -  Unidade: ${item.unidadeMedida}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = quantidade,
                        onValueChange = { quantidade = it },
                        label = { Text("Quantidade") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            if (viewModel.adicionarItem(itemSelecionado, quantidade)) {
                                quantidade = ""
                                itemSelecionado = null
                            }
                        }
                    ) { Text("Adicionar") }
                }
            }
        }
        SecaoCard(titulo = "Itens do pedido") {
            if (estado.linhas.isEmpty()) {
                Text("Nenhum item adicionado.", modifier = Modifier.padding(top = 8.dp))
            } else {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    estado.linhas.forEach { linha ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(linha.item.descricao, fontWeight = FontWeight.Medium)
                                Text(
                                    "${linha.quantidade} ${linha.item.unidadeMedida} x " +
                                        "${formatarMoeda(linha.item.valorUnit)} = ${formatarMoeda(linha.subtotal)}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            IconButton(onClick = { viewModel.removerLinha(linha) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remover")
                            }
                        }
                        Divider()
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Itens adicionados: ${estado.quantidadeItens}")
                Text("Total dos itens: ${formatarMoeda(estado.totalItens)}", fontWeight = FontWeight.Bold)
            }
        }
        SecaoCard(titulo = "Condicao de pagamento") {
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = estado.condicao == CondicaoPagamento.A_VISTA,
                    onClick = { viewModel.definirCondicao(CondicaoPagamento.A_VISTA) },
                    label = { Text("A vista (-5%)") }
                )
                FilterChip(
                    selected = estado.condicao == CondicaoPagamento.A_PRAZO,
                    onClick = { viewModel.definirCondicao(CondicaoPagamento.A_PRAZO) },
                    label = { Text("A prazo (+5%)") }
                )
            }

            if (estado.condicao == CondicaoPagamento.A_PRAZO) {
                OutlinedTextField(
                    value = parcelasTexto,
                    onValueChange = {
                        parcelasTexto = it.filter { c -> c.isDigit() }
                        viewModel.definirParcelas(parcelasTexto.toIntOrNull() ?: 0)
                    },
                    label = { Text("Quantidade de parcelas") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                if (estado.parcelas.isNotEmpty()) {
                    Text(
                        "Parcelas:",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    estado.parcelas.forEachIndexed { indice, valor ->
                        Text("Parcela ${indice + 1}: ${formatarMoeda(valor)}")
                    }
                }
            }
        }
        SecaoCard(titulo = "Endereco de entrega") {
            AppDropdown(
                rotulo = "Selecione o endereco",
                opcoes = enderecos,
                selecionado = estado.enderecoEntrega,
                textoDe = { "Cod ${it.codigo} - ${it.resumo()}" },
                aoSelecionar = { viewModel.selecionarEnderecoEntrega(it) },
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                "Frete: ${formatarMoeda(estado.frete)}",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "(Toledo-PR: gratis - outra cidade: R$ 20,00 - outro estado: + R$ 50,00)",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("VALOR TOTAL DO PEDIDO", style = MaterialTheme.typography.labelLarge)
                Text(
                    formatarMoeda(estado.total),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = {
                    viewModel.limparTudo()
                    limparCamposLocais()
                },
                modifier = Modifier.weight(1f)
            ) { Text("Limpar") }
            Button(
                onClick = {
                    viewModel.concluirPedido {
                        limparCamposLocais()
                    }
                },
                modifier = Modifier.weight(2f)
            ) { Text("Concluir pedido") }
        }
        estado.consulta?.let { consulta ->
            AlertDialog(
                onDismissRequest = { viewModel.fecharConsulta() },
                confirmButton = {
                    TextButton(onClick = { viewModel.fecharConsulta() }) { Text("Fechar") }
                },
                title = { Text("Pedido no ${consulta.pedido.codigo}") },
                text = {
                    Column {
                        Text("Cliente (cod): ${consulta.pedido.clienteCodigo}")
                        Text("Condicao: " +
                            if (consulta.pedido.condicaoPagamento == CondicaoPagamento.A_VISTA)
                                "A vista" else "A prazo (${consulta.pedido.numeroParcelas}x)")
                        Text("Itens: ${consulta.pedido.quantidadeItens}")
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                        consulta.itens.forEach { item ->
                            Text("- ${item.descricao} - ${item.quantidade} x " +
                                formatarMoeda(item.valorUnitario))
                        }
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                        Text("Frete: ${formatarMoeda(consulta.pedido.valorFrete)}")
                        Text(
                            "Total: ${formatarMoeda(consulta.pedido.valorTotal)}",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }

        SecaoCard(titulo = "Pedidos cadastrados (${pedidos.size})") {
            if (pedidos.isEmpty()) {
                Text("Nenhum pedido cadastrado.", modifier = Modifier.padding(top = 8.dp))
            } else {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    pedidos.forEach { pedidoComItens ->
                        val pedido = pedidoComItens.pedido
                        Text(
                            "Pedido no ${pedido.codigo} - ${pedido.quantidadeItens} itens - " +
                                formatarMoeda(pedido.valorTotal),
                            fontWeight = FontWeight.Medium
                        )
                        Divider(modifier = Modifier.padding(vertical = 6.dp))
                    }
                }
            }
        }
    }
}
