package com.kiisafezone.mip.contracts

import com.kiisafezone.mip.domain.MatchEventSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive

class MatchEventContractTest {
    @Test fun contractRoundTripAndDomainMappingPreserveProvenance() {
        val contract = MatchEventContract(
            id = "e-1",
            matchId = "m-1",
            type = "NEWER_CLIENT_EVENT",
            gameTimeMs = 500,
            recordedAtEpochMs = 1_800_000_000_000,
            source = MatchEventSource.SYNTHETIC,
            confidence = 0.75,
        )
        val json = Json { ignoreUnknownKeys = true }
        val decoded = json.decodeFromString<MatchEventContract>(json.encodeToString(contract))
        assertEquals(contract, decoded)
        assertEquals(contract.type, decoded.toDomain().type)
        assertEquals(contract.source, decoded.toDomain().source)
        assertEquals("e-1", decoded.toDomain().toContract().id)
    }

    @Test fun unknownOptionalPayloadFieldsSurviveDecode() {
        val json = Json { ignoreUnknownKeys = true }
        val encoded = """{"id":"e","matchId":"m","type":"FUTURE","gameTimeMs":1,"recordedAtEpochMs":2,"source":"MANUAL","confidence":1.0,"payload":{"futureKey":"value"}}"""
        val decoded = json.decodeFromString<MatchEventContract>(encoded)
        assertEquals("value", decoded.payload["futureKey"]?.jsonPrimitive?.content)
    }
}
