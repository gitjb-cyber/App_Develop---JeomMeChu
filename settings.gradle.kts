pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // Kakao 지도 SDK Maven 저장소 추가 (Kotlin DSL)
        maven {
            url = uri("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "JeomMeChu"
include(":app")
