plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.plugins.kotlin.jvm.toDep())
    implementation(libs.plugins.grammarKit.toDep())
    implementation(libs.plugins.sqldelight.toDep())
    implementation(libs.plugins.licensee.toDep())
    implementation(libs.plugins.mavencentral.toDep())
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}
