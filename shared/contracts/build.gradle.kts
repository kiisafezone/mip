plugins { alias(libs.plugins.android.library); alias(libs.plugins.kotlin.multiplatform) }
kotlin { androidTarget(); sourceSets { commonMain.dependencies {} } }
android { namespace = "com.kiisafezone.mip.shared.contracts"; compileSdk = 36; defaultConfig { minSdk = 26 } }
