dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        mavenCentral()
        gradlePluginPortal()
    }

    versionCatalogs.register("libs") {
        from(files("../gradle/libs.versions.toml"))
    }
}

rootProject.name = "build-logic"
