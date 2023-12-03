package app.softwork.sqldelight.db2dialect

import app.cash.sqldelight.dialect.api.*
import app.softwork.sqldelight.db2dialect.grammar.*
import com.alecstrong.sql.psi.core.*
import com.intellij.icons.*
import com.squareup.kotlinpoet.*
import javax.swing.*

public class Db2Dialect : SqlDelightDialect {
    override val icon: Icon = AllIcons.Providers.DB2
    override fun setup() {
        SqlParserUtil.reset()

        Db2ParserUtil.reset()
        Db2ParserUtil.overrideSqlParser()
    }

    override fun typeResolver(parentResolver: TypeResolver): TypeResolver = Db2TypeResolver(parentResolver)

    override val runtimeTypes: RuntimeTypes = RuntimeTypes(
        ClassName("app.cash.sqldelight.driver.jdbc", "JdbcCursor"),
        ClassName("app.cash.sqldelight.driver.jdbc", "JdbcPreparedStatement")
    )

    override val asyncRuntimeTypes: RuntimeTypes = RuntimeTypes(
        ClassName("app.cash.sqldelight.driver.r2dbc", "R2dbcCursor"),
        ClassName("app.cash.sqldelight.driver.r2dbc", "R2dbcPreparedStatement")
    )

    /*
     * Specify predefined system tables in SQL
     */
    public val predefinedSystemSchema: List<String> = Db2Dialect.predefinedSystemSchema

    public companion object {
        /*
        * Specify predefined system tables in SQL
        */
        public val predefinedSystemSchema: List<String> = listOf(
            """
            CREATE TABLE SYSIBM.SYSDUMMY1 (
              IBMREQD CHAR(1) NOT NULL
            );
            """.trimIndent()
        )
    }
}
