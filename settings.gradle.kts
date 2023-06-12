pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("MyRepos")
    id("com.gradle.enterprise") version "3.12.5"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        if (System.getenv("CI") != null) {
            publishAlways()
            tag("CI")
        }
    }
}

rootProject.name = "sqldelight-db2"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":sqldelight-db2-dialect")

// include(":testing")
