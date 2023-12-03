package app.softwork.sqldelight.db2dialect.async

import app.softwork.sqldelight.db2dialect.testR2dbcDriver
import kotlin.test.*

class AsyncTesting {
    @Test
    fun select() = testR2dbcDriver { driver ->
        TestingDBAsync.Schema.create(driver)
        val db = TestingDBAsync(driver)

        assertEquals(emptyList(), db.fooQueries.getAll().executeAsList())
        db.fooQueries.new(Foo(42, "Foo"))
        assertEquals(listOf(Foo(42, "Foo")), db.fooQueries.getAll().executeAsList())

        db.fooQueries.create(Foo(100, "Bar"))
        assertEquals(listOf(Foo(42, "Foo"), Foo(100, "Bar")), db.fooQueries.getAll().executeAsList())
    }
}
