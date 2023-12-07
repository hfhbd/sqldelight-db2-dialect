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
        val create = Foo(42, -1, time, "Foo")
        db.fooQueries.new(create)
        assertEquals(listOf(create), db.fooQueries.getAll().executeAsList())

        db.fooQueries.create(Foo(100, -1, time, "Bar"))
        val getAll = db.fooQueries.getAll().executeAsList()
        assertEquals(2, getAll.size)
        assertEquals(create, getAll[0])
        with(getAll[1]) {
            assertEquals(100, id)
            assertEquals(42, id2)
            assertTrue(time > LocalDateTime.of(2023, 1, 1, 1, 1))
            assertEquals("Bar", name)
        }
    }
}
