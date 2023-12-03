package app.softwork.sqldelight.db2dialect

import kotlin.test.*

class Testing {
    @Test
    fun select() {
        val driver = jdbcDriver()
        TestingDB.Schema.create(driver)
        val db = TestingDB(driver)

        assertEquals(emptyList(), db.fooQueries.getAll().executeAsList())
        db.fooQueries.new(Foo(42, "Foo"))
        assertEquals(listOf(Foo(42, "Foo")), db.fooQueries.getAll().executeAsList())

        db.fooQueries.create(Foo(100, "Bar"))
        assertEquals(listOf(Foo(42, "Foo"), Foo(100, "Bar")), db.fooQueries.getAll().executeAsList())
    }
}
