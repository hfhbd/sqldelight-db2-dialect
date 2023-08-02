plugins {
    kotlin("jvm")
    id("app.cash.sqldelight")
}

dependencies {
    implementation(libs.sqldelight.jdbcDriver)
    implementation(libs.db2.driver)

    testImplementation(kotlin("test"))
    testImplementation(libs.testcontainers)
    testRuntimeOnly(libs.logback)
}

sqldelight {
    databases.register("TestingDB") {
        dialect(projects.sqldelightDb2Dialect)
        packageName.set("app.softwork.sqldelight.db2dialect")
    }
}
