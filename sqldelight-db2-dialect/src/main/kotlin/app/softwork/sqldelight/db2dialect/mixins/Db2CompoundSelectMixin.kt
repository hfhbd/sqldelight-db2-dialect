package app.softwork.sqldelight.db2dialect.mixins

import com.alecstrong.sql.psi.core.psi.*
import com.alecstrong.sql.psi.core.psi.impl.*
import com.intellij.lang.*
import com.intellij.psi.util.*

internal abstract class Db2CompoundSelectMixin(node: ASTNode): SqlCompoundSelectStmtImpl(node) {
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
}
