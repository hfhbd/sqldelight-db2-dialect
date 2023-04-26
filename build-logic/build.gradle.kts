plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
    implementation("com.alecstrong:grammar-kit-composer:0.1.12")
    implementation("io.github.gradle-nexus:publish-plugin:1.1.0")
    implementation("org.jetbrains.kotlinx:binary-compatibility-validator:0.13.1")
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
