package app.softwork.sqldelight.db2dialect

import app.softwork.sqldelight.db2dialect.DB2.driver
import java.time.LocalDateTime
import kotlin.test.*

class Testing {
    @Test
    fun select() {
        TestingDB.Schema.create(driver)
        val db = TestingDB(driver)

        assertEquals(emptyList(), db.fooQueries.getAll().executeAsList())
        db.fooQueries.new(Foo(42, -1, LocalDateTime.MIN, "Foo"))
        assertEquals(listOf(Foo(42, -1, LocalDateTime.MIN, "Foo")), db.fooQueries.getAll().executeAsList())

        db.fooQueries.create(Foo(100, -1, LocalDateTime.MIN, "Bar"))
        assertEquals(
            listOf(Foo(42, -1, LocalDateTime.MIN, "Foo"), Foo(100, -1, LocalDateTime.MIN, "Bar")),
            db.fooQueries.getAll().executeAsList()
        )
    }
}
