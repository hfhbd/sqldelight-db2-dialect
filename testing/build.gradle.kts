plugins {
    kotlin("jvm")
    id("app.cash.sqldelight")
}

dependencies {
    implementation("app.cash.sqldelight:jdbc-driver:2.0.0-alpha04")

    testImplementation(kotlin("test-junit"))
    testImplementation("org.testcontainers:db2:1.17.5")
    testImplementation("com.ibm.db2:jcc:11.5.7.0")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.4.3")
}

sqldelight {
    database("TestingDB") {
        dialect(project(":sqldelight-db2-dialect"))
        packageName = "app.softwork.sqldelight.db2dialect"
    }
}
