package com.kiisafezone.mip

import org.junit.Assert.assertEquals
import org.junit.Test

class BuildConfigurationTest {
    @Test
    fun versionNameIsPinned() {
        assertEquals("0.1.0", BuildConfig.VERSION_NAME)
    }
}
