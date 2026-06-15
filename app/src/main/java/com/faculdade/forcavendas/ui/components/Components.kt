package com.faculdade.forcavendas.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

fun formatarMoeda(valor: Double): String {
    val formato = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return formato.format(valor)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppDropdown(
    rotulo: String,
    opcoes: List<T>,
    selecionado: T?,
    textoDe: (T) -> String,
    aoSelecionar: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }
    val textoAtual = selecionado?.let(textoDe) ?: ""

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = textoAtual,
            onValueChange = {},
            readOnly = true,
            label = { Text(rotulo) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        if (opcoes.isNotEmpty()) {
            androidx.compose.material3.ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                opcoes.forEach { opcao ->
                    DropdownMenuItem(
                        text = { Text(textoDe(opcao)) },
                        onClick = {
                            aoSelecionar(opcao)
                            expandido = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SecaoCard(titulo: String, modifier: Modifier = Modifier, conteudo: @Composable () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            conteudo()
        }
    }
}
