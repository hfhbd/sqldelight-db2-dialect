package app.softwork.sqldelight.db2dialect

import com.alecstrong.sql.psi.core.psi.*
import com.alecstrong.sql.psi.core.psi.impl.*
import com.intellij.lang.*
import com.intellij.psi.util.*

internal abstract class SelectStmtMixin(node: ASTNode) : SqlSelectStmtImpl(node) {
    override fun getValuesExpressionList(): List<SqlValuesExpression> =
        PsiTreeUtil.getChildrenOfTypeAsList(this, SqlValuesExpression::class.java)
}
