package app.softwork.sqldelight.db2dialect.mixins

import app.softwork.sqldelight.db2dialect.grammar.psi.*
import com.alecstrong.sql.psi.core.SqlAnnotationHolder
import com.alecstrong.sql.psi.core.psi.*
import com.alecstrong.sql.psi.core.psi.QueryElement.QueryResult
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil


private fun PsiElement.printTree(printer: (String) -> Unit) {
  printer("$this\n")
  children.forEach { child ->
    child.printTree { printer("  $it") }
  }
}

internal abstract class SetStmtMixin(
  node: ASTNode,
) : SqlCompositeElementImpl(node),
  Db2SetStmt {

  private val compoundSelectStmt get() = PsiTreeUtil.getChildOfType(this, SqlCompoundSelectStmt::class.java)

  private val Db2SetSetterClause.exprList: List<SqlExpr> get() = PsiTreeUtil.getChildrenOfTypeAsList(this, SqlExpr::class.java)

  override fun queryAvailable(child: PsiElement): Collection<QueryResult> {
    if (compoundSelectStmt == null && setSetterClause == null) {
      return emptyList()
    }
    val compoundSelectStmt = compoundSelectStmt ?: return setSetterClause!!.exprList.map {
      QueryResult(it)
    }
    return if (child in compoundSelectStmt.children) {
      compoundSelectStmt.queryAvailable(child)
    } else emptyList()
  }

  override fun queryExposed(): Collection<QueryResult> {
    val queryExposed = compoundSelectStmt?.queryExposed() ?: setSetterClause!!.exprList.map {
      QueryResult(it)
    }
    val columnNames = this.hostVariableList
    return queryExposed.map { result ->
      result.copy(
        columns = result.columns.mapIndexed { index, it ->
          val named = it.element as? NamedElement ?: return@mapIndexed it
          val foundHostVariable = columnNames.getOrNull(index) ?: return@mapIndexed  it
          it.copy(element = RenamedColumn(named, foundHostVariable.hostVariableId!!.name))
        }
      )
    }
  }

  private class RenamedColumn(origin: NamedElement, private val newName: String): NamedElement by origin {
    override fun getName(): String = newName
    override fun setName(name: String): Nothing = error("Not supported")
  }

  override fun annotate(annotationHolder: SqlAnnotationHolder) {
    val select = compoundSelectStmt
    if (select != null) {
      println(select.selectStmtList.size)
      val f = select.selectStmtList
      printTree { print(it) }
      select.printTree { print(it) }
      println(f)
      select.annotate(annotationHolder)
      for (sqlSelectStmt in select.selectStmtList) {
        val selectInto = (sqlSelectStmt as Db2SelectStmt).selectIntoClause
        if (selectInto != null) {
          annotationHolder.createErrorAnnotation(
            selectInto,
            "Cannot use SET with SELECT INTO",
          )
        }
      }
    }
    setSetterClause?.annotate(annotationHolder)

    val hostVariables = hostVariableList.size
    val resultColumns = queryExposed().sumOf { it.columns.size + it.synthesizedColumns.size }
    if (hostVariables > resultColumns) {
      annotationHolder.createErrorAnnotation(
        this,
        "Cannot bind $hostVariables host variables to $resultColumns result columns",
      )
    }
  }
}
