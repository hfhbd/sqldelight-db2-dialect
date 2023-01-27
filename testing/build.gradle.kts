plugins {
    kotlin("jvm")
    app.softwork.sqldelight
}

dependencies {
    implementation("app.cash.sqldelight:jdbc-driver:2.0.0-alpha05")

    testImplementation(kotlin("test"))
    testImplementation("org.testcontainers:db2:1.17.6")
    testImplementation("com.ibm.db2:jcc:11.5.8.0")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.4.5")
}

sqldelight {
    databases.register("TestingDB") {
        dialect(projects.sqldelightDb2Dialect)
        packageName.set("app.softwork.sqldelight.db2dialect")
    }
}
