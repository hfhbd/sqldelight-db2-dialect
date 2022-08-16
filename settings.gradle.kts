pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "sqldelight-db2-dialect"

include(":dialect")
project(":dialect").name = "sqldelight-db2-dialect"

include(":testing")
