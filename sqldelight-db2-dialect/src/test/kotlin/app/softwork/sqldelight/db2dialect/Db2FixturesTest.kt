package app.softwork.sqldelight.db2dialect

import com.alecstrong.sql.psi.test.fixtures.FixturesTest
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.io.File

@RunWith(Parameterized::class)
class Db2FixturesTest(name: String, fixtureRoot: File) : FixturesTest(name, fixtureRoot) {
    override val replaceRules = arrayOf(
        "TEXT" to "VARCHAR(8)",
    )

    override fun setupDialect() {
        Db2Dialect().setup()
    }

    companion object {
        private val fixtures = arrayOf("src/test/fixtures_db2")

        @Suppress("unused")
        // Used by Parameterized JUnit runner reflectively.
        @Parameters(name = "{0}")
        @JvmStatic
        fun parameters() = fixtures.flatMap { fixtureFolder ->
            File(fixtureFolder).listFiles()!!
                .filter { it.isDirectory }
                .map { arrayOf(it.name, it) }
        } + ansiFixtures
    }
}
