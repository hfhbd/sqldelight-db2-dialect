package app.softwork.sqldelight.db2dialect.mixins

import app.softwork.sqldelight.db2dialect.grammar.Db2Parser
import app.softwork.sqldelight.db2dialect.grammar.psi.Db2HostVariableId
import com.alecstrong.sql.psi.core.psi.SqlNamedElementImpl
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder

internal abstract class HostVariableMixin(
  node: ASTNode,
) : SqlNamedElementImpl(node), Db2HostVariableId {
  override val parseRule: (builder: PsiBuilder, level: Int) -> Boolean = Db2Parser::host_variable_id_real
}
