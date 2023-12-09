package app.softwork.sqldelight.db2dialect

import app.cash.sqldelight.dialect.api.*
import app.softwork.sqldelight.db2dialect.grammar.psi.Db2ExtensionStmt
import app.softwork.sqldelight.db2dialect.grammar.psi.Db2SelectStmt
import app.softwork.sqldelight.db2dialect.grammar.psi.Db2TypeName
import com.alecstrong.sql.psi.core.psi.*
import com.intellij.psi.PsiElement

internal class Db2TypeResolver(private val parentResolver: TypeResolver) : TypeResolver by parentResolver {
    override fun definitionType(typeName: SqlTypeName): IntermediateType {
        check(typeName is Db2TypeName)
        with(typeName) {
            return IntermediateType(when {
                approximateNumericDataType != null -> PrimitiveType.REAL
                binaryStringDataType != null -> PrimitiveType.BLOB
                dateDataType != null -> {
                    when (dateDataType!!.firstChild.text) {
                        "DATE" -> Db2Type.DATE
                        "TIME" -> Db2Type.TIME
                        "TIMESTAMP" -> if (dateDataType!!.node.getChildren(null)
                                .any { it.text == "WITH" }
                        ) Db2Type.TIMESTAMP_TIMEZONE else Db2Type.TIMESTAMP

                        "TIMESTAMPTZ" -> Db2Type.TIMESTAMP_TIMEZONE
                        else -> throw IllegalArgumentException("Unknown date type ${dateDataType!!.text}")
                    }
                }

                tinyIntDataType != null -> Db2Type.TINY_INT
                smallIntDataType != null -> Db2Type.SMALL_INT
                intDataType != null -> Db2Type.INTEGER
                bigIntDataType != null -> Db2Type.BIG_INT
                fixedPointDataType != null -> PrimitiveType.INTEGER
                characterStringDataType != null -> PrimitiveType.TEXT
                booleanDataType != null -> Db2Type.BOOL
                bitStringDataType != null -> PrimitiveType.BLOB
                intervalDataType != null -> PrimitiveType.BLOB
                else -> throw IllegalArgumentException("Unknown kotlin type for sql type ${typeName.text}")
            })
        }
    }

    override fun queryWithResults(sqlStmt: SqlStmt): QueryWithResults? {
        val ext = sqlStmt.extensionStmt
        if (ext != null) {
            val set = (ext as Db2ExtensionStmt).setStmt
            val select = set.child<SqlCompoundSelectStmt>()
            return if (select != null) {
                SelectQueryable(select)
            } else {
                SetQueryWithResults(set)
            }
        }
        val compoundSelectStmt = sqlStmt.compoundSelectStmt
        return if (compoundSelectStmt != null) {
            for (stmt in compoundSelectStmt.selectStmtList) {
                stmt as Db2SelectStmt
                if (stmt.selectIntoClause != null) {
                    return CreateNewTableClass(compoundSelectStmt)
                }
            }
            return SelectQueryable(compoundSelectStmt)
        } else parentResolver.queryWithResults(sqlStmt)
    }

    override fun resolvedType(expr: SqlExpr): IntermediateType = when(expr) {
        is SqlLiteralExpr -> when (expr.literalValue.text) {
            "CURRENT_DATE", "CURRENT DATE" -> IntermediateType(Db2Type.DATE)
            "CURRENT_TIMESTAMP", "CURRENT TIMESTAMP" -> IntermediateType(Db2Type.TIMESTAMP)
            else -> parentResolver.resolvedType(expr)
        }
        else -> parentResolver.resolvedType(expr)
    }
}

private inline fun <reified T : PsiElement> PsiElement.child(): T? {
    var current = this@child
    while (true) {
        val firstChild = current.firstChild ?: break
        if (firstChild is T) {
            return firstChild
        }
        current = firstChild
    }
    return null
}

private class CreateNewTableClass(
    override val select: SqlCompoundSelectStmt,
) : QueryWithResults {
    override var statement: SqlAnnotatedElement = select
    override val pureTable: NamedElement? = null
}
