plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
    implementation("com.alecstrong:grammar-kit-composer:0.1.11")
    implementation("io.github.gradle-nexus:publish-plugin:1.1.0")
    implementation("org.jetbrains.kotlinx:binary-compatibility-validator:0.12.1")
    implementation("app.softwork.sqldelight:gradle-plugin:2.0.0-SNAPSHOT")
    implementation("app.cash.licensee:licensee-gradle-plugin:1.6.0")
}

gradlePlugin {
    plugins {
        register("MyRepos") {
            id = "MyRepos"
            implementationClass = "MyRepos"
        }
    }
}
