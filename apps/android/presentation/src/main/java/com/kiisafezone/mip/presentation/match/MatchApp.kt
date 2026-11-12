package com.kiisafezone.mip.presentation.match

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kiisafezone.mip.domain.MatchResult
import kotlinx.coroutines.launch

@Composable
fun MatchApp(viewModel: MatchSessionViewModel) {
    val state by viewModel.uiState.collectAsState()
    var creatingMatch by remember { mutableStateOf(false) }
    var exportContent by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
    ) { uri ->
        val content = exportContent
        if (uri != null && content != null) {
            runCatching {
                context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { it.write(content) }
                    ?: error("Unable to open the selected export destination.")
            }.onFailure { viewModel.reportError(it) }
        }
        exportContent = null
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column {
            state.errorMessage?.let { message ->
                TextButton(onClick = viewModel::clearError) { Text("Dismiss: $message") }
            }
            val selected = state.selectedSession
            when {
                selected != null -> MatchDetailScreen(
                    session = selected,
                    events = state.events,
                    onBack = viewModel::closeMatch,
                    onUpdate = viewModel::saveExistingMatch,
                    onAddEvent = viewModel::addEvent,
                    onCorrectEvent = viewModel::correctEvent,
                    onDeleteEvent = viewModel::deleteEvent,
                    onDeleteMatch = viewModel::deleteMatch,
                    onPauseOrResume = viewModel::pauseOrResumeMatch,
                    onComplete = viewModel::completeMatch,
                    onExport = {
                        scope.launch {
                            runCatching { viewModel.exportSelectedMatch() }
                                .onSuccess {
                                    exportContent = it
                                    exportLauncher.launch("mip-match-export.json")
                                }
                                .onFailure { viewModel.reportError(it) }
                        }
                    },
                )
                creatingMatch -> NewMatchScreen(
                    onSave = { hero, role, mode, result, notes ->
                        viewModel.saveMatch(null, hero, role, mode, result, notes)
                        creatingMatch = false
                    },
                    onCancel = { creatingMatch = false },
                )
                else -> MatchListScreen(
                    sessions = state.sessions,
                    onCreate = { creatingMatch = true },
                    onOpen = { viewModel.openMatch(it.id) },
                )
            }
        }
    }
}
