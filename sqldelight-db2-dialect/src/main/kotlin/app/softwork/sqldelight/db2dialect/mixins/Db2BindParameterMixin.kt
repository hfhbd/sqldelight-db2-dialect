package app.softwork.sqldelight.db2dialect.mixins

import app.cash.sqldelight.dialect.grammar.mixins.BindParameterMixin
import com.intellij.lang.ASTNode

public abstract class Db2BindParameterMixin(node: ASTNode) : BindParameterMixin(node) {
    override fun replaceWith(isAsync: Boolean, index: Int): String = when {
        isAsync -> "$$index"
        else -> "?"
    }
}
