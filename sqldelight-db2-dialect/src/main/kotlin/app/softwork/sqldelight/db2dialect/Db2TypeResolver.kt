package app.softwork.sqldelight.db2dialect

import app.cash.sqldelight.dialect.api.*
import app.softwork.sqldelight.db2dialect.grammar.psi.*
import com.alecstrong.sql.psi.core.psi.*

internal class Db2TypeResolver(private val parentResolver: TypeResolver) : TypeResolver by parentResolver {
    override fun definitionType(typeName: SqlTypeName): IntermediateType {
        check(typeName is Db2TypeName)
        with(typeName) {
            return when {
                approximateNumericDataType != null -> IntermediateType(PrimitiveType.REAL)
                binaryStringDataType != null -> IntermediateType(PrimitiveType.BLOB)
                dateDataType != null -> IntermediateType(PrimitiveType.TEXT)
                tinyIntDataType != null -> IntermediateType(Db2Type.TINY_INT)
                smallIntDataType != null -> IntermediateType(Db2Type.SMALL_INT)
                intDataType != null -> IntermediateType(Db2Type.INTEGER)
                bigIntDataType != null -> IntermediateType(Db2Type.BIG_INT)
                fixedPointDataType != null -> IntermediateType(PrimitiveType.INTEGER)
                characterStringDataType != null -> IntermediateType(PrimitiveType.TEXT)
                booleanDataType != null -> IntermediateType(Db2Type.BOOL)
                bitStringDataType != null -> IntermediateType(PrimitiveType.BLOB)
                intervalDataType != null -> IntermediateType(PrimitiveType.BLOB)
                else -> throw IllegalArgumentException("Unknown kotlin type for sql type ${typeName.text}")
            }
        }
    }

    override fun functionType(functionExpr: SqlFunctionExpr): IntermediateType? =
        parentResolver.functionType(functionExpr)
}
