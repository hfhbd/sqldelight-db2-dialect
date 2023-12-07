package app.softwork.sqldelight.db2dialect.mixins

import com.alecstrong.sql.psi.core.psi.impl.SqlLiteralValueImpl
import com.intellij.lang.ASTNode

internal abstract class LiteralValueMixin(node: ASTNode): SqlLiteralValueImpl(node)
