pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://www.jetbrains.com/intellij-repository/releases")
        maven(url = "https://cache-redirector.jetbrains.com/intellij-dependencies")
    }
}

rootProject.name = "sqldelight-db2-dialect"

include(":dialect")
project(":dialect").name = "sqldelight-db2-dialect"

include(":testing")
