plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget()
    jvm()
    sourceSets {
        commonMain.dependencies { implementation(libs.kotlinx.serialization.json) }
        commonTest.dependencies { implementation(libs.kotlin.test) }
    }
}

android {
    namespace = "com.kiisafezone.mip.shared.domain"
    compileSdk = 36
    defaultConfig { minSdk = 26 }
}
