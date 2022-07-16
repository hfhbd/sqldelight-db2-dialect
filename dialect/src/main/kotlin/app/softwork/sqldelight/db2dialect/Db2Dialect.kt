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

        val currentElementCreation = Db2ParserUtil.createElement
        Db2ParserUtil.createElement = {
            when (it.elementType) {
                else -> currentElementCreation(it)
            }
        }
    }

    override fun typeResolver(parentResolver: TypeResolver): TypeResolver = Db2TypeResolver(parentResolver)

    override val runtimeTypes: RuntimeTypes = RuntimeTypes(
        ClassName("app.cash.sqldelight.driver.jdbc", "JdbcCursor"),
        ClassName("app.cash.sqldelight.driver.jdbc", "JdbcPreparedStatement")
    )

    override val asyncRuntimeTypes: RuntimeTypes
        get() = throw UnsupportedOperationException("DB2 does not support an async driver")
}
