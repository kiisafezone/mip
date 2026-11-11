plugins { alias(libs.plugins.android.library); alias(libs.plugins.kotlin.multiplatform) }
kotlin {
    androidTarget()
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:domain"))
            implementation(project(":shared:contracts"))
        }
    }
}
android { namespace = "com.kiisafezone.mip.shared.engine"; compileSdk = 36; defaultConfig { minSdk = 26 } }
