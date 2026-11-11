package com.kiisafezone.mip.contracts

/** Marker for data exchanged across MIP module boundaries. */
interface VersionedContract {
    val schemaVersion: Int
}
