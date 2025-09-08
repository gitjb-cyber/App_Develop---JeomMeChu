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

val versionPropsFile = rootProject.file("version.properties")
val versionProps = Properties().apply {
    if (versionPropsFile.exists()) load(versionPropsFile.inputStream())
}
// 이번 빌드가 release 인지 설정 단계에서 판별
val isReleaseBuild = gradle.startParameter.taskNames.any { it.contains("Release", ignoreCase = true) }

// 현재 코드 읽기
var versionCodeFromFile = (versionProps["VERSION_CODE"]?.toString()?.toInt() ?: 1)

// 만약 release 빌드라면 설정 단계에서 즉시 +1 하고 파일에 저장
if (isReleaseBuild) {
    versionCodeFromFile += 1
    versionProps["VERSION_CODE"] = versionCodeFromFile.toString()
    // .use { } 로 안전 저장 (Properties는 ISO-8859-1로 저장됨)
    versionPropsFile.writer().use { writer ->
        versionProps.store(writer, null)
    }
    println("📦 versionCode bumped for THIS release build → $versionCodeFromFile")
}

// versionName은 수동 관리(파일 그대로 사용)
val versionNameValue: String = versionProps["VERSION_NAME"]?.toString() ?: "1.0.0"

android {
    namespace = "com.jbandroid.jeommechu"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jbandroid.jeommechu"
        minSdk = 26
        targetSdk = 36
        versionCode = versionCodeFromFile
        versionName = versionNameValue

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