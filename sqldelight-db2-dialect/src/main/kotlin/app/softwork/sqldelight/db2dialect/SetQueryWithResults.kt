package app.softwork.sqldelight.db2dialect

import app.cash.sqldelight.dialect.api.*
import app.softwork.sqldelight.db2dialect.grammar.psi.*
import com.alecstrong.sql.psi.core.psi.*

public class SetQueryWithResults(setStmt: Db2SetStmt) : QueryWithResults {
    override var statement: SqlAnnotatedElement = setStmt
    override val select: Db2SetStmt = setStmt
    override val pureTable: NamedElement? = null
}
