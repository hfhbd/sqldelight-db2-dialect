pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("MyRepos")
    id("com.gradle.enterprise") version "3.14.1"
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
