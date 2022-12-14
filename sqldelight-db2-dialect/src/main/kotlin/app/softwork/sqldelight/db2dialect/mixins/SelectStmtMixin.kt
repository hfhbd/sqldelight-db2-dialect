package app.softwork.sqldelight.db2dialect.mixins

import app.softwork.sqldelight.db2dialect.grammar.psi.*
import com.alecstrong.sql.psi.core.ModifiableFileLazy
import com.alecstrong.sql.psi.core.SqlAnnotationHolder
import com.alecstrong.sql.psi.core.psi.*
import com.alecstrong.sql.psi.core.psi.QueryElement.QueryResult
import com.alecstrong.sql.psi.core.psi.impl.SqlSelectStmtImpl
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil

private inline fun <reified T : PsiElement> PsiElement.parentOfType(withSelf: Boolean = false): T? {
  return PsiTreeUtil.getParentOfType(this, T::class.java, !withSelf)
}

internal abstract class Db2SelectStmtMixin(
  node: ASTNode,
) : SqlSelectStmtImpl(node),
  Db2SelectStmt,
  FromQuery {
  override fun getSelectIntoClause(): Db2SelectIntoClause? {
    TODO("Not yet implemented")
  }
  /**
   * During some resolution steps we don't care about the parent's projection and can safely ignore
   * it to avoid recursing too far.
   */
  private var ignoreParentProjection = false

  private val queryExposed = ModifiableFileLazy {
    if (valuesExpressionList.isNotEmpty()) {
      return@ModifiableFileLazy listOf(QueryResult(null, valuesExpressionList.first().exprList.map {
        QueryElement.QueryColumn(
          it
        )
      }))
    }
    return@ModifiableFileLazy listOf(
      QueryResult(
        null,
        columns = resultColumnList.flatMap { resultColumn ->
          resultColumn.queryExposed().flatMap { queryResult ->
            queryResult.columns.map {
              if (exprList.size > 0 && it.element.nonNullIn(exprList[0])) it.copy(nullable = false)
              else it
            }
          }
        },
      ),
    )
  }

  override fun queryAvailable(child: PsiElement): Collection<QueryResult> {
    if (child in exprList) {
      val available = fromQuery().map { it.copy(adjacent = true) } +
        super.queryAvailable(this).map { it.copy(adjacent = false) }
      if (ignoreParentProjection) return available

      val projection = parentOfType<SqlCompoundSelectStmt>()?.queryExposed()?.map { selectStmt ->
        selectStmt.copy(
          adjacent = false,
          columns = selectStmt.columns.filter { projectionColumn ->
            // Avoid including any projection columns that would create name collisions.
            (projectionColumn.element as? PsiNamedElement)?.name !in
              available.flatMap {
                it.columns.mapNotNull { (it.element as? PsiNamedElement)?.name }
              }
          },
        )
      } ?: emptyList()

      return available + projection
    }
    if (child in resultColumnList) {
      return fromQuery().map { it.copy(adjacent = true) } +
        super.queryAvailable(this).map { it.copy(adjacent = false) }
    }
    if (child == joinClause) return super.queryAvailable(child)
    return super.queryAvailable(child)
  }

  override fun queryExposed() = queryExposed.forFile(containingFile)

  override fun fromQuery(): Collection<QueryResult> {
    joinClause?.let {
      return it.queryExposed()
    }
    return emptyList()
  }

  private fun PsiElement.nonNullIn(whereExpr: SqlExpr): Boolean {
    if (this is SqlColumnAlias) return source().nonNullIn(whereExpr)
    if (this is SqlResultColumn) return expr?.nonNullIn(whereExpr) ?: false
    if (this is SqlColumnExpr) return columnName.nonNullIn(whereExpr)
    if (this !is SqlColumnName) return false
    return when (whereExpr) {
      is SqlParenExpr -> nonNullIn(whereExpr.expr ?: return false)
      is SqlIsExpr -> {
        val (lhs, rhs) = whereExpr.exprList
        (lhs is SqlColumnExpr && lhs.columnName.isSameAs(this)) &&
          (rhs is SqlLiteralExpr && rhs.literalValue.node.findChildByType(SqlTypes.NULL) != null) &&
          whereExpr.node.findChildByType(SqlTypes.NOT) != null
      }
      is SqlBinaryAndExpr -> nonNullIn(whereExpr.getExprList()[0]) || nonNullIn(whereExpr.getExprList()[1])
      is SqlBinaryOrExpr -> nonNullIn(whereExpr.getExprList()[0]) && nonNullIn(whereExpr.getExprList()[1])
      else -> false
    }
  }

  private fun SqlColumnName.isSameAs(other: SqlColumnName): Boolean {
    try {
      ignoreParentProjection = true
      if (this == other) return true
      val thisRef = reference?.resolve() ?: return false
      if (thisRef == other) return true
      val otherRef = other.reference?.resolve() ?: return false
      return thisRef == otherRef
    } finally {
      ignoreParentProjection = false
    }
  }

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
