package com.kiisafezone.mip.presentation.match

import com.kiisafezone.mip.domain.MatchEvent
import com.kiisafezone.mip.domain.MatchSession

data class MatchUiState(
    val sessions: List<MatchSession> = emptyList(),
    val selectedSession: MatchSession? = null,
    val events: List<MatchEvent> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
