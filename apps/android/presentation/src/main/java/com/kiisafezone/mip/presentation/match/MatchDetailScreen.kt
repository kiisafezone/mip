package com.kiisafezone.mip.presentation.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.kiisafezone.mip.domain.MatchEventTypes
import com.kiisafezone.mip.domain.MatchResult
import com.kiisafezone.mip.domain.MatchSession

@Composable
fun MatchDetailScreen(
    session: MatchSession,
    events: List<MatchEvent>,
    onBack: () -> Unit,
    onUpdate: (MatchSession, String, String, String, String, String, MatchResult, String, String) -> Unit,
    onAddEvent: (String, String, String) -> Unit,
    onCorrectEvent: (MatchEvent, String, String, String, String) -> Unit,
    onDeleteEvent: (MatchEventId) -> Unit,
    onDeleteMatch: () -> Unit,
    onExport: () -> Unit,
) {
    var type by remember { mutableStateOf(MatchEventTypes.HERO_DIED) }
    var time by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }
    var editingMatch by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TextButton(onClick = onBack) { Text("← All matches") }
        Text(session.heroName ?: "Match review", style = MaterialTheme.typography.headlineSmall)
        Text(listOfNotNull(session.role, session.mode, session.gameVersionId, session.durationMs?.let { (it / 60_000).toString() + ":" + ((it / 1_000) % 60).toString().padStart(2, '0') }, session.result?.name).joinToString(" · "))
        Text("${events.size} recorded events")
        session.notes?.let { Text(it) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { editingMatch = true }) { Text("Edit details") }
            TextButton(onClick = onExport) { Text("Export JSON") }
        }
        TextButton(onClick = { confirmDelete = true }) { Text("Delete match") }

        Text("Record 3–5 key moments", style = MaterialTheme.typography.titleLarge)
        Text("Add manually observed deaths and objective/contest markers with their match-relative times. Leave unknown times unrecorded.")
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(
                MatchEventTypes.HERO_DIED,
                MatchEventTypes.OBJECTIVE_OBSERVED,
                MatchEventTypes.TACTICAL_NOTE,
            ).forEach { option ->
                TextButton(onClick = { type = option }) {
                    Text(if (type == option) "• ${option.replace('_', ' ')}" else option.replace('_', ' '))
                }
            }
        }
        androidx.compose.material3.OutlinedTextField(
            time, { time = it }, label = { Text("Match time (mm:ss)") }, modifier = Modifier.fillMaxWidth(),
        )
        androidx.compose.material3.OutlinedTextField(
            detail,
            { detail = it },
            label = {
                Text(
                    when (type) {
                        MatchEventTypes.OBJECTIVE_OBSERVED -> "Objective (e.g. TURTLE contest)"
                        MatchEventTypes.TACTICAL_NOTE -> "Note"
                        else -> "Optional context"
                    },
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = { onAddEvent(type, time, detail); detail = "" },
            modifier = Modifier.fillMaxWidth(),
            enabled = time.isNotBlank() && (type == MatchEventTypes.HERO_DIED || detail.isNotBlank()),
        ) { Text("Add event") }

        MatchTimelineScreen(events, onCorrectEvent, onDeleteEvent)
    }

    if (editingMatch) {
        AlertDialog(
            onDismissRequest = { editingMatch = false },
            title = { Text("Edit match metadata") },
            text = {
                NewMatchScreen(
                    existing = session,
                    onSave = { hero, role, mode, patch, duration, result, notes, revisionReason ->
                        onUpdate(session, hero, role, mode, patch, duration, result, notes, revisionReason)
                        editingMatch = false
                    },
                    onCancel = { editingMatch = false },
                )
            },
            confirmButton = {},
        )
    }
    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete this match?") },
            text = { Text("This permanently deletes the match, its events, and its local correction history.") },
            confirmButton = {
                Button(onClick = { onDeleteMatch(); confirmDelete = false }) { Text("Delete match") }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancel") } },
        )
    }
}
