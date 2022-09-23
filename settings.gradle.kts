pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    }
    includeBuild("build-logic")
}

plugins {
    id("MyRepos")
}

rootProject.name = "sqldelight-db2"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":sqldelight-db2-dialect")

include(":testing")
