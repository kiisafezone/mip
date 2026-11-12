package com.kiisafezone.mip.presentation.match

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiisafezone.mip.domain.MatchSession
import java.text.DateFormat
import java.util.Date

@Composable
fun MatchListScreen(
    sessions: List<MatchSession>,
    onCreate: () -> Unit,
    onOpen: (MatchSession) -> Unit,
) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("MIP · Match review", style = MaterialTheme.typography.headlineMedium)
        Text("Record your own match notes. Entries are player-reported, not live game data.")
        Button(onClick = onCreate, modifier = Modifier.fillMaxWidth()) { Text("New match") }
        Text("Recent matches", style = MaterialTheme.typography.titleLarge)
        if (sessions.isEmpty()) {
            Text("No matches recorded yet. Create your first match to begin.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sessions, key = { it.id.value }) { session ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onOpen(session) },
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(session.heroName ?: "Unspecified hero", style = MaterialTheme.typography.titleMedium)
                            Text(listOfNotNull(session.role, session.mode, session.result?.name).joinToString(" · "))
                            Text(DateFormat.getDateTimeInstance().format(Date(session.startedAtEpochMillis)))
                        }
                    }
                }
            }
        }
    }
}
