# Module sqldelight-db2-dialect

A DB2 dialect for SqlDelight using JDBC.

- [Source code](https://github.com/hfhbd/sqldelight-db2-dialect)

## Install

This package is uploaded to MavenCentral and supports the JVM.


````kotlin
plugins {
    kotlin("jvm") version "1.9.0"
    id("app.cash.sqldelight") version "2.0.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("app.cash.sqldelight:jdbc-driver:2.0.0")

    testImplementation(kotlin("test-junit"))
    testImplementation("org.testcontainers:db2:1.17.3")
    testImplementation("com.ibm.db2:jcc:11.5.7.0")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.11")
}

sqldelight {
    databases.register("Db2Database") {
        dialect("app.softwork:sqldelight-db2-dialect:LATEST")
    }
}
````

## License

Apache 2
