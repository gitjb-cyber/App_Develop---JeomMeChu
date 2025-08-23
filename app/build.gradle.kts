import java.util.Properties
import java.io.File
import java.io.FileNotFoundException

fun getApiKey(key: String): String {
    val properties = Properties()
    val localPropsFile = File(rootDir, "local.properties")
    if (localPropsFile.exists()) {
        properties.load(localPropsFile.inputStream())
    } else {
        throw FileNotFoundException("local.properties 파일이 없습니다.")
    }

    return properties.getProperty(key)
        ?: throw IllegalArgumentException("local.properties에 ${key} 키가 없습니다.")
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.jbandroid.jeommechu"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jbandroid.jeommechu"
        minSdk = 35
        targetSdk = 36
        versionCode = 3
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "KAKAO_NATIVE_APP_KEY", getApiKey("KAKAO_NATIVE_APP_KEY"))
        buildConfigField("String", "KAKAO_REST_API_KEY", getApiKey("KAKAO_REST_API_KEY"))
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore/my-release-key.jks")
            storePassword = project.properties["KEYSTORE_PASSWORD"] as String
            keyAlias = "key_Jeom"
            keyPassword = project.properties["KEY_PASSWORD"] as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.gms.play.services.location)

    val composeBom = platform(libs.androidx.compose.bom.v20250601)

    // 플랫폼 의존성 (Compose BOM)
    implementation(composeBom)

    // Jetpack Compose
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.material)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // Lifecycle, ViewModel
    implementation(libs.androidx.lifecycle.runtime.ktx.v290)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Activity + Navigation
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)

    // Room (with KSP)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.common.jvm)
    ksp(libs.androidx.room.compiler)

    // Retrofit & Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)

    // Coroutine
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    // Google Play Services
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)

    // Accompanist (Jetpack Compose 보조 유틸)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.flowlayout)

    // 기타 Jetpack
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.coordinatorlayout)

    // Hilt Navigation for Compose (DI 사용 시)
    implementation(libs.androidx.hilt.navigation.compose)

    // 테스트 관련
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(composeBom)
    androidTestImplementation(libs.ui.test.junit4)
}