package app.softwork.sqldelight.db2dialect

import app.softwork.sqldelight.db2dialect.DB2.driver
import java.time.LocalDateTime
import kotlin.test.*

class Testing {
    @Test
    fun select() {
        TestingDB.Schema.create(driver)
        val db = TestingDB(driver)

        val time = LocalDateTime.of(2023, 10, 10, 1, 1)

        assertEquals(emptyList(), db.fooQueries.getAll().executeAsList())
        db.fooQueries.new(Foo(42, -1, time, "Foo"))
        assertEquals(listOf(Foo(42, -1, time, "Foo")), db.fooQueries.getAll().executeAsList())

        db.fooQueries.create(Foo(100, -1, time, "Bar"))
        assertEquals(
            listOf(Foo(42, -1, time, "Foo"), Foo(100, -1, time, "Bar")),
            db.fooQueries.getAll().executeAsList()
        )
    }
}
