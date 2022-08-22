pluginManagement {
    repositories {
        maven(url ="file://${rootDir.absolutePath}/localMaven")
        gradlePluginPortal()
    }
}

rootProject.name = "sqldelight-db2"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":sqldelight-db2-dialect")

include(":testing")
