package com.kiisafezone.mip.presentation.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiisafezone.mip.domain.MatchEvent
import com.kiisafezone.mip.domain.MatchEventId
import com.kiisafezone.mip.domain.MatchEventRepository
import com.kiisafezone.mip.domain.MatchEventRevision
import com.kiisafezone.mip.domain.MatchEventSource
import com.kiisafezone.mip.domain.MatchEventTypes
import com.kiisafezone.mip.domain.MatchEventValidation
import com.kiisafezone.mip.domain.MatchExportRepository
import com.kiisafezone.mip.domain.MatchResult
import com.kiisafezone.mip.domain.MatchSession
import com.kiisafezone.mip.domain.MatchSessionId
import com.kiisafezone.mip.domain.MatchSessionRepository
import com.kiisafezone.mip.domain.MatchSessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.UUID

class MatchSessionViewModel(
    private val sessions: MatchSessionRepository,
    private val events: MatchEventRepository,
    private val exporter: MatchExportRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MatchUiState(isLoading = true))
    val uiState: StateFlow<MatchUiState> = _uiState.asStateFlow()
    private val json = Json { encodeDefaults = true }

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            runCatching { sessions.getAll() }
                .onSuccess { _uiState.value = _uiState.value.copy(sessions = it, isLoading = false) }
                .onFailure { showError(it) }
        }
    }

    fun openMatch(id: MatchSessionId) {
        viewModelScope.launch {
            runCatching {
                val session = sessions.get(id)
                val matchEvents = if (session == null) emptyList() else events.getForMatch(id)
                session to matchEvents
            }.onSuccess { (session, matchEvents) ->
                _uiState.value = _uiState.value.copy(selectedSession = session, events = matchEvents, errorMessage = null)
            }.onFailure { showError(it) }
        }
    }

    fun closeMatch() {
        _uiState.value = _uiState.value.copy(selectedSession = null, events = emptyList(), errorMessage = null)
        refresh()
    }

    fun saveMatch(
        existing: MatchSession?,
        heroName: String,
        role: String,
        mode: String,
        result: MatchResult,
        notes: String,
    ) {
        if (heroName.isBlank() || role.isBlank()) {
            reportError(IllegalArgumentException("Hero and role are required."))
            return
        }
        val session = if (existing == null) {
            MatchSession(
                id = MatchSessionId(UUID.randomUUID().toString()),
                startedAtEpochMillis = System.currentTimeMillis(),
                heroName = heroName.trim(),
                role = role.trim(),
                mode = mode.trim().ifBlank { null },
                result = result,
                notes = notes.trim().ifBlank { null },
            )
        } else {
            existing.copy(
                heroName = heroName.trim(),
                role = role.trim(),
                mode = mode.trim().ifBlank { null },
                result = result,
                notes = notes.trim().ifBlank { null },
            )
        }
        viewModelScope.launch {
            runCatching { sessions.save(session) }
                .onSuccess {
                    openMatch(session.id)
                    refresh()
                }
                .onFailure { showError(it) }
        }
    }

    fun saveExistingMatch(
        existing: MatchSession,
        heroName: String,
        role: String,
        mode: String,
        result: MatchResult,
        notes: String,
    ) = saveMatch(existing, heroName, role, mode, result, notes)

    fun pauseOrResumeMatch() {
        val current = _uiState.value.selectedSession ?: return
        val changed = runCatching {
            when (current.status) {
                MatchSessionStatus.ACTIVE -> current.pause()
                MatchSessionStatus.PAUSED -> current.resume()
                MatchSessionStatus.COMPLETED -> current
            }
        }.getOrElse { showError(it); return }
        saveSessionAndRefresh(changed)
    }

    fun completeMatch() {
        val current = _uiState.value.selectedSession ?: return
        runCatching { current.complete(System.currentTimeMillis()) }
            .onSuccess(::saveSessionAndRefresh)
            .onFailure(::showError)
    }

    private fun saveSessionAndRefresh(session: MatchSession) {
        viewModelScope.launch {
            runCatching { sessions.save(session) }
                .onSuccess { openMatch(session.id); refresh() }
                .onFailure { showError(it) }
        }
    }

    fun deleteMatch() {
        val current = _uiState.value.selectedSession ?: return
        viewModelScope.launch {
            runCatching { sessions.delete(current.id) }
                .onSuccess { closeMatch() }
                .onFailure { showError(it) }
        }
    }

    fun addEvent(type: String, timeText: String, detail: String) {
        val session = _uiState.value.selectedSession ?: return
        val gameTime = parseGameTime(timeText) ?: run {
            reportError(IllegalArgumentException("Enter match time as mm:ss."))
            return
        }
        val payload = buildJsonObject {
            when (type) {
                MatchEventTypes.OBJECTIVE_OBSERVED -> put("objectiveId", detail.trim().ifBlank { "UNKNOWN" })
                MatchEventTypes.TACTICAL_NOTE -> put("text", detail.trim().ifBlank { "No note provided" })
                MatchEventTypes.HERO_DIED -> if (detail.isNotBlank()) put("context", detail.trim())
                MatchEventTypes.SPELL_USED -> put("abilityId", detail.trim().ifBlank { "UNKNOWN" })
            }
        }
        val event = MatchEvent(
            id = MatchEventId(UUID.randomUUID().toString()),
            matchId = session.id,
            type = type,
            gameTimeMs = gameTime,
            recordedAtEpochMs = System.currentTimeMillis(),
            source = MatchEventSource.MANUAL,
            confidence = 1.0,
            validation = MatchEventValidation.UNREVIEWED,
            payload = payload,
        )
        viewModelScope.launch {
            runCatching { events.save(event) }
                .onSuccess { openMatch(session.id) }
                .onFailure { showError(it) }
        }
    }

    fun correctEvent(event: MatchEvent, timeText: String, reason: String) {
        val session = _uiState.value.selectedSession ?: return
        val gameTime = parseGameTime(timeText) ?: run {
            reportError(IllegalArgumentException("Enter match time as mm:ss."))
            return
        }
        if (reason.isBlank()) {
            reportError(IllegalArgumentException("A correction reason is required."))
            return
        }
        val corrected = event.copy(
            gameTimeMs = gameTime,
            validation = MatchEventValidation.CORRECTED,
        )
        val revision = MatchEventRevision(
            id = UUID.randomUUID().toString(),
            eventId = event.id,
            matchId = event.matchId,
            beforeSnapshotJson = json.encodeToString(event),
            afterSnapshotJson = json.encodeToString(corrected),
            reason = reason.trim(),
            changedAtEpochMs = System.currentTimeMillis(),
        )
        viewModelScope.launch {
            runCatching { events.correct(corrected, revision) }
                .onSuccess { openMatch(session.id) }
                .onFailure { showError(it) }
        }
    }

    fun deleteEvent(eventId: MatchEventId) {
        val matchId = _uiState.value.selectedSession?.id ?: return
        viewModelScope.launch {
            runCatching { events.delete(eventId) }
                .onSuccess { openMatch(matchId) }
                .onFailure { showError(it) }
        }
    }

    suspend fun exportSelectedMatch(): String {
        val session = requireNotNull(_uiState.value.selectedSession) { "No match selected." }
        return exporter.exportMatch(session.id, System.currentTimeMillis())
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun reportError(error: Throwable) = showError(error)

    private fun showError(error: Throwable) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = error.message ?: "An unexpected error occurred.",
        )
    }

    private fun parseGameTime(value: String): Long? {
        val parts = value.trim().split(":")
        if (parts.size != 2) return null
        val minutes = parts[0].toLongOrNull() ?: return null
        val seconds = parts[1].toLongOrNull() ?: return null
        if (minutes < 0 || seconds !in 0..59) return null
        return (minutes * 60 + seconds) * 1_000
    }
}
