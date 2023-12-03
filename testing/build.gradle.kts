plugins {
    kotlin("jvm")
    id("app.cash.sqldelight")
}

kotlin.jvmToolchain(8)

dependencies {
    implementation(libs.sqldelight.jdbcDriver)
    implementation(libs.sqldelight.r2dbcDriver)
    implementation(libs.db2.driver)
    implementation(libs.db2.driver.r2dbc)
    implementation(libs.coroutines.reactive)

    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)
    testImplementation(libs.testcontainers)
    testRuntimeOnly(libs.logback)
}

sqldelight {
    databases.register("TestingDB") {
        dialect(projects.sqldelightDb2Dialect)
        packageName.set("app.softwork.sqldelight.db2dialect")
    }

    databases.register("TestingDBAsync") {
        dialect(projects.sqldelightDb2Dialect)
        packageName.set("app.softwork.sqldelight.db2dialect.async")
        generateAsync.set(true)
        srcDirs.setFrom("src/main/sqldelight-async")
    }
}
