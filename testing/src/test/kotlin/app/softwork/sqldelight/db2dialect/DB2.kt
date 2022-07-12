package app.softwork.sqldelight.db2dialect

import app.cash.sqldelight.driver.jdbc.*
import com.ibm.db2.jcc.*
import org.testcontainers.containers.*

object DB2 {
    val driver = run {
        val container = Db2Container("ibmcom/db2:11.5.7.0").acceptLicense()
        container.start()
        DB2SimpleDataSource().apply {
            databaseName = "test"
            user = "db2inst1"
            serverName = ""
            portNumber = container.firstMappedPort
            setPassword("foobar1234")
            driverType = 4
        }.asJdbcDriver()
    }
}
