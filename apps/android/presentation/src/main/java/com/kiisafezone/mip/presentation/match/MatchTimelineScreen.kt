package com.kiisafezone.mip.presentation.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiisafezone.mip.domain.MatchEvent
import com.kiisafezone.mip.domain.MatchEventId

@Composable
fun MatchTimelineScreen(
    events: List<MatchEvent>,
    onCorrect: (MatchEvent, String, String) -> Unit,
    onDelete: (MatchEventId) -> Unit,
) {
    var editing by remember { mutableStateOf<MatchEvent?>(null) }
    var newTime by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var deleting by remember { mutableStateOf<MatchEvent?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Timeline")
        if (events.isEmpty()) Text("No events recorded yet.")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(events, key = { it.id.value }) { event ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("${event.gameTimeMs / 60_000}:${((event.gameTimeMs / 1_000) % 60).toString().padStart(2, '0')} · ${event.type}")
                        Text("Player-reported · ${event.validation.name}")
                        Text(event.payload.toString())
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = {
                                editing = event
                                newTime = "${event.gameTimeMs / 60_000}:${((event.gameTimeMs / 1_000) % 60).toString().padStart(2, '0')}"
                                reason = ""
                            }) { Text("Correct") }
                            TextButton(onClick = { deleting = event }) { Text("Delete") }
                        }
                    }
                }
            }
        }
    }

    editing?.let { event ->
        AlertDialog(
            onDismissRequest = { editing = null },
            title = { Text("Correct event") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(newTime, { newTime = it }, label = { Text("Correct time (mm:ss)") })
                    OutlinedTextField(reason, { reason = it }, label = { Text("Correction reason") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCorrect(event, newTime, reason)
                        editing = null
                    },
                    enabled = reason.isNotBlank(),
                ) { Text("Save correction") }
            },
            dismissButton = { TextButton(onClick = { editing = null }) { Text("Cancel") } },
        )
    }
    deleting?.let { event ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Delete event?") },
            text = { Text("This permanently deletes the event from this local match record.") },
            confirmButton = {
                Button(onClick = { onDelete(event.id); deleting = null }) { Text("Delete") }
            },
            dismissButton = { TextButton(onClick = { deleting = null }) { Text("Cancel") } },
        )
    }
}
