package com.kiisafezone.mip.engine

import com.kiisafezone.mip.domain.AbilityId
import com.kiisafezone.mip.domain.CooldownDefinition
import com.kiisafezone.mip.domain.GameVersionId

sealed interface CooldownDefinitionResult {
    data class Available(val definition: CooldownDefinition) : CooldownDefinitionResult
    data class Unavailable(val reason: Reason) : CooldownDefinitionResult
    enum class Reason { GAME_VERSION_UNKNOWN, DEFINITION_MISSING }
}

/** Resolves only an exact game-version + ability match; never falls back. */
fun resolveCooldownDefinition(
    definitions: Iterable<CooldownDefinition>,
    gameVersionId: GameVersionId?,
    abilityId: AbilityId,
): CooldownDefinitionResult {
    if (gameVersionId == null) {
        return CooldownDefinitionResult.Unavailable(CooldownDefinitionResult.Reason.GAME_VERSION_UNKNOWN)
    }
    val match = definitions.firstOrNull {
        it.gameVersionId == gameVersionId && it.abilityId == abilityId
    } ?: return CooldownDefinitionResult.Unavailable(CooldownDefinitionResult.Reason.DEFINITION_MISSING)
    return CooldownDefinitionResult.Available(match)
}
