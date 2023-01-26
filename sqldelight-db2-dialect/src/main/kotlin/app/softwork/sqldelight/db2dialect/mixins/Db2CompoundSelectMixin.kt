package app.softwork.sqldelight.db2dialect.mixins

import app.cash.sqldelight.dialect.api.*
import app.softwork.sqldelight.db2dialect.grammar.psi.*
import com.alecstrong.sql.psi.core.psi.*
import com.alecstrong.sql.psi.core.psi.impl.*
import com.intellij.lang.*
import com.intellij.psi.util.*

internal abstract class Db2CompoundSelectMixin(node: ASTNode) : SqlCompoundSelectStmtImpl(node) {
    private val node = PsiTreeUtil.getChildOfType(this, SqlCompoundSelectStmt::class.java)

    override fun getCompoundOperatorList(): List<SqlCompoundOperator> = PsiTreeUtil.getChildrenOfTypeAsList(
        node,
        SqlCompoundOperator::class.java
    )

    override fun getLimitingTermList(): List<SqlLimitingTerm> = PsiTreeUtil.getChildrenOfTypeAsList(
        node,
        SqlLimitingTerm::class.java
    )

    override fun getOrderingTermList(): List<SqlOrderingTerm> = PsiTreeUtil.getChildrenOfTypeAsList(
        node,
        SqlOrderingTerm::class.java
    )

    override fun getSelectStmtList(): List<SqlSelectStmt> = PsiTreeUtil.getChildrenOfTypeAsList(
        node,
        SqlSelectStmt::class.java
    )

    override fun getWithClause(): SqlWithClause? = PsiTreeUtil.getChildOfType(node, SqlWithClause::class.java)

    fun queryWithResults(): QueryWithResults {
        for (stmt in selectStmtList) {
            stmt as Db2SelectStmt
            if (stmt.selectIntoClause != null) {
                return CreateNewTableClass(this)
            }
        }
        return SelectQueryable(this)
    }

    private class CreateNewTableClass(
        override val select: SqlCompoundSelectStmt,
    ) : QueryWithResults {
        override var statement: SqlAnnotatedElement = select
        override val pureTable: NamedElement? = null
    }
}
