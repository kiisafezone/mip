package com.kiisafezone.mip.presentation.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiisafezone.mip.domain.MatchResult
import com.kiisafezone.mip.domain.MatchSession

@Composable
fun NewMatchScreen(
    existing: MatchSession? = null,
    onSave: (String, String, String, MatchResult, String) -> Unit,
    onCancel: (() -> Unit)? = null,
) {
    var hero by remember(existing?.id) { mutableStateOf(existing?.heroName.orEmpty()) }
    var role by remember(existing?.id) { mutableStateOf(existing?.role.orEmpty()) }
    var mode by remember(existing?.id) { mutableStateOf(existing?.mode.orEmpty()) }
    var notes by remember(existing?.id) { mutableStateOf(existing?.notes.orEmpty()) }
    var result by remember(existing?.id) { mutableStateOf(existing?.result) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(if (existing == null) "New match" else "Edit match details")
        OutlinedTextField(hero, { hero = it }, label = { Text("Hero") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(role, { role = it }, label = { Text("Role") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(mode, { mode = it }, label = { Text("Mode (optional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(notes, { notes = it }, label = { Text("Notes (optional)") }, modifier = Modifier.fillMaxWidth())
        Text("Result")
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            MatchResult.entries.forEach { option ->
                Button(onClick = { result = option }) { Text(if (result == option) "✓ ${option.name}" else option.name) }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { onSave(hero, role, mode, requireNotNull(result), notes) },
                enabled = hero.isNotBlank() && role.isNotBlank() && result != null,
            ) { Text("Save match") }
            if (onCancel != null) Button(onClick = onCancel) { Text("Cancel") }
        }
    }
}
