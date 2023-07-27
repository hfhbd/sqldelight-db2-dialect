package app.softwork.sqldelight.db2dialect.mixins

import app.softwork.sqldelight.db2dialect.grammar.psi.Db2SelectStmt
import com.alecstrong.sql.psi.core.SqlAnnotationHolder
import com.alecstrong.sql.psi.core.psi.FromQuery
import com.alecstrong.sql.psi.core.psi.SqlBindExpr
import com.alecstrong.sql.psi.core.psi.SqlTypes
import com.alecstrong.sql.psi.core.psi.impl.SqlSelectStmtImpl
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

internal abstract class Db2SelectStmtMixin(
    node: ASTNode,
) : SqlSelectStmtImpl(node),
    Db2SelectStmt,
    FromQuery {

    override fun annotate(annotationHolder: SqlAnnotationHolder) {
        super.annotate(annotationHolder)

        val invalidGroupByBindExpression = exprList.find { child ->
            child is SqlBindExpr &&
                    PsiTreeUtil.findSiblingBackward(child, SqlTypes.HAVING, null) == null &&
                    PsiTreeUtil.findSiblingBackward(child, SqlTypes.BY, null) != null &&
                    PsiTreeUtil.findSiblingBackward(child, SqlTypes.GROUP, null) != null
        }
        if (invalidGroupByBindExpression != null) {
            annotationHolder.createErrorAnnotation(
                invalidGroupByBindExpression,
                "Cannot bind the name of a column in a GROUP BY clause",
            )
        }

        if (valuesExpressionList.isNotEmpty()) {
            val size = valuesExpressionList[0].exprList.size
            valuesExpressionList.drop(1).forEach {
                if (it.exprList.size != size) {
                    annotationHolder.createErrorAnnotation(
                        it,
                        "Unexpected number of columns in values found: ${it.exprList.size} expected: $size",
                    )
                }
            }
        }
        val into = selectIntoClause
        if (into != null) {
            val hostVariables = into.hostVariableList.size
            val resultColumns = queryExposed().sumOf { it.columns.size + it.synthesizedColumns.size }
            if (hostVariables > resultColumns) {
                annotationHolder.createErrorAnnotation(
                    into,
                    "Cannot bind $hostVariables host variables to $resultColumns result columns",
                )
            }
        }
    }
}
