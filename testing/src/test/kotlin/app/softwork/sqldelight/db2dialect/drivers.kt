package app.softwork.sqldelight.db2dialect

import app.cash.sqldelight.db.*
import app.cash.sqldelight.driver.jdbc.*
import app.cash.sqldelight.driver.r2dbc.R2dbcDriver
import com.ibm.db2.jcc.*
import com.ibm.db2.r2dbc.DB2ConnectionConfiguration
import com.ibm.db2.r2dbc.DB2ConnectionFactory
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.testcontainers.containers.*

fun jdbcDriver(): JdbcDriver {
    val container = Db2Container("icr.io/db2_community/db2").acceptLicense()
    container.start()
    return DB2SimpleDataSource().apply {
        databaseName = "test"
        user = "db2inst1"
        serverName = ""
        portNumber = container.firstMappedPort
        setPassword("foobar1234")
        driverType = 4
    }.asJdbcDriver()
}

fun testR2dbcDriver(action: suspend TestScope.(R2dbcDriver) -> Unit): TestResult {
    val container = Db2Container("icr.io/db2_community/db2").acceptLicense()
    container.start()
    val config: DB2ConnectionConfiguration = DB2ConnectionConfiguration.builder()
        .database("test")
        .host("localhost")
        .port(container.firstMappedPort)
        .username("db2inst1")
        .password("foobar1234")
        .build()

    return runTest {
        val driver = R2dbcDriver(DB2ConnectionFactory(config).create().awaitFirst())
        driver.use {
            action(it)
        }
    }
}
