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
import kotlin.time.Duration.Companion.minutes

private val container = Db2Container("ibmcom/db2:11.5.7.0").acceptLicense()

fun jdbcDriver(): JdbcDriver {
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
    container.start()
    val config: DB2ConnectionConfiguration = DB2ConnectionConfiguration.builder()
        .database("test")
        .host("localhost")
        .port(container.firstMappedPort)
        .username("db2inst1")
        .password("foobar1234")
        .build()

    return runTest {
        val driver = backgroundScope.R2dbcDriver(DB2ConnectionFactory(config).create().awaitFirst())
        driver.use {
            action(it)
        }
    }
}
