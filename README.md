# Module sqldelight-db2-dialect

A DB2 dialect for SqlDelight using JDBC.

- [Source code](https://github.com/hfhbd/sqldelight-db2-dialect)

## Install

This package is uploaded to MavenCentral and supports the JVM.


````kotlin
repositories {
    mavenCentral()
}

sqldelight {
    database("Db2Database") {
        dialect("app.softwork:sqldelight-db2-dialect:LATEST")
    }
}
````

## License

Apache 2
