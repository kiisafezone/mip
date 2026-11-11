package com.kiisafezone.mip.domain

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class GameVersionId(val value: String) {
    init { require(value.isNotBlank()) { "Game version ID must not be blank." } }
}

@JvmInline
@Serializable
value class AbilityId(val value: String) {
    init { require(value.isNotBlank()) { "Ability ID must not be blank." } }
}

@Serializable
enum class MechanicsSourceKind { SYNTHETIC_FIXTURE, USER_AUTHORED, AUTHORIZED_PUBLIC_SOURCE }

@Serializable
data class DataProvenance(
    val sourceKind: MechanicsSourceKind,
    val sourceReference: String,
    val retrievedAtEpochMs: Long? = null,
    val reviewedAtEpochMs: Long? = null,
    val reviewedBy: String? = null,
    val notes: String? = null,
) {
    init {
        require(sourceReference.isNotBlank())
        require(retrievedAtEpochMs == null || retrievedAtEpochMs >= 0)
        require(reviewedAtEpochMs == null || reviewedAtEpochMs >= 0)
        require(reviewedBy == null || reviewedBy.isNotBlank())
    }
}

@Serializable
data class CooldownDefinition(
    val gameVersionId: GameVersionId,
    val abilityId: AbilityId,
    val durationMs: Long,
    val provenance: DataProvenance,
    val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
) {
    init {
        require(durationMs >= 0)
        require(schemaVersion > 0)
    }
    companion object { const val CURRENT_SCHEMA_VERSION: Int = 1 }
}
