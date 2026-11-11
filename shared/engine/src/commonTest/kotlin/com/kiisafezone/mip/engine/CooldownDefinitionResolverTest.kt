package com.kiisafezone.mip.engine

import com.kiisafezone.mip.domain.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CooldownDefinitionResolverTest {
    private val v1 = GameVersionId("synthetic-1")
    private val v2 = GameVersionId("synthetic-2")
    private val ability = AbilityId("synthetic-ability-a")
    private val definition = CooldownDefinition(
        gameVersionId = v1,
        abilityId = ability,
        durationMs = 12_345,
        provenance = DataProvenance(
            sourceKind = MechanicsSourceKind.SYNTHETIC_FIXTURE,
            sourceReference = "fixtures/synthetic-cooldowns.json",
            notes = "Invented test data; not copied from game assets or live game data.",
        ),
    )

    @Test fun exactVersionAndAbilityResolve() {
        assertEquals(CooldownDefinitionResult.Available(definition),
            resolveCooldownDefinition(listOf(definition), v1, ability))
    }

    @Test fun unknownVersionIsUnavailable() {
        assertEquals(CooldownDefinitionResult.Unavailable(CooldownDefinitionResult.Reason.GAME_VERSION_UNKNOWN),
            resolveCooldownDefinition(listOf(definition), null, ability))
    }

    @Test fun differentVersionDoesNotFallBack() {
        assertEquals(CooldownDefinitionResult.Unavailable(CooldownDefinitionResult.Reason.DEFINITION_MISSING),
            resolveCooldownDefinition(listOf(definition), v2, ability))
    }

    @Test fun missingAbilityDoesNotFallBack() {
        assertIs<CooldownDefinitionResult.Unavailable>(
            resolveCooldownDefinition(listOf(definition), v1, AbilityId("other-ability")))
    }
}
