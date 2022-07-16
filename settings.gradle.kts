pluginManagement {
    repositories {
        gradlePluginPortal()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    }
}

rootProject.name = "sqldelight-db2-dialect"

include(":dialect")
project(":dialect").name = "sqldelight-db2-dialect"

include(":testing")
