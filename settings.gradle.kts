pluginManagement {
    includeBuild("gradle/build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("MyRepos")
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("com.gradle.develocity") version "4.1"
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")
        val isCI = providers.environmentVariable("CI").isPresent
        publishing {
            onlyIf { isCI }
        }
        tag("CI")
    }
}

rootProject.name = "sqldelight-db2"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(":sqldelight-db2-dialect")
include(":testing")
