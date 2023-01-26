package app.softwork.sqldelight.db2dialect

import app.cash.sqldelight.dialect.api.*
import app.softwork.sqldelight.db2dialect.grammar.psi.*
import app.softwork.sqldelight.db2dialect.mixins.*
import com.alecstrong.sql.psi.core.psi.*

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

    override fun functionType(functionExpr: SqlFunctionExpr): IntermediateType? =
        parentResolver.functionType(functionExpr)

    override fun queryWithResults(sqlStmt: SqlStmt): QueryWithResults? {
        val ext = sqlStmt.extensionStmt
        if (ext != null) {
            val set = (ext as Db2ExtensionStmt).setStmt
            set.compoundSelectStmtInternal?.let { SelectQueryable(it) }
            return SetQueryWithResults(set)
        }
        val compoundSelectStmt = sqlStmt.compoundSelectStmt
        return if (compoundSelectStmt is Db2CompoundSelectMixin) {
            compoundSelectStmt.queryWithResults()
        } else parentResolver.queryWithResults(sqlStmt)
    }
}
