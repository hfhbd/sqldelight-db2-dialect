plugins {
    kotlin("jvm")
    id("app.cash.sqldelight")
}

kotlin.jvmToolchain(8)

dependencies {
    implementation(libs.sqldelight.jdbcDriver)
    implementation(libs.db2.driver)
}

testing.suites.named("test", JvmTestSuite::class) {
    useKotlinTest()

    dependencies {
        implementation(libs.testcontainers)
        runtimeOnly(libs.logback)
    }
}

sqldelight {
    databases.register("TestingDB") {
        dialect(projects.sqldelightDb2Dialect)
        packageName.set("app.softwork.sqldelight.db2dialect")
    }
}
