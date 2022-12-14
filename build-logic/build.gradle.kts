plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.22")
    implementation("com.alecstrong:grammar-kit-composer:0.2.0-SNAPSHOT")
    implementation("io.github.gradle-nexus:publish-plugin:1.1.0")
    implementation("org.jetbrains.kotlinx.binary-compatibility-validator:org.jetbrains.kotlinx.binary-compatibility-validator.gradle.plugin:0.12.1")
    implementation("app.cash.sqldelight:gradle-plugin:2.0.0-SNAPSHOT")
    implementation("app.cash.licensee:licensee-gradle-plugin:1.7.0-SNAPSHOT")
}

gradlePlugin {
    plugins {
        create("MyRepos") {
            id = "MyRepos"
            implementationClass = "MyRepos"
        }
        create("repos") {
            id = "repos"
            implementationClass = "Repos"
        }
    }
}
