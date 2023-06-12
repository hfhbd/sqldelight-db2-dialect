/**
 *    Copyright Square Inc. 2023
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package app.cash.sqldelight.dialect.api

import app.softwork.sqldelight.db2dialect.mixins.parentOfType
import com.alecstrong.sql.psi.core.psi.NamedElement
import com.alecstrong.sql.psi.core.psi.QueryElement.QueryColumn
import com.alecstrong.sql.psi.core.psi.Queryable
import com.alecstrong.sql.psi.core.psi.SqlAnnotatedElement
import com.alecstrong.sql.psi.core.psi.SqlCompoundSelectStmt
import com.alecstrong.sql.psi.core.psi.SqlCreateViewStmt
import com.alecstrong.sql.psi.core.psi.SqlCteTableName
import com.alecstrong.sql.psi.core.psi.SqlViewName
import com.intellij.psi.util.PsiTreeUtil

internal class SelectQueryable(
  override val select: SqlCompoundSelectStmt,
  override var statement: SqlAnnotatedElement = select,
) : QueryWithResults {

  /**
   * If this query is a pure select from a table (virtual or otherwise), this returns the LazyQuery
   * which points to that table (Pure meaning it has exactly the same columns in the same order).
   */
  override val pureTable: NamedElement? by lazy {
    fun List<QueryColumn>.flattenCompounded(): List<QueryColumn> {
      return map { column ->
        if (column.compounded.none { it.element != column.element || it.nullable != column.nullable }) {
          column.copy(compounded = emptyList())
        } else {
          column
        }
      }
    }

    val pureColumns = select.queryExposed().singleOrNull()?.columns?.flattenCompounded()

    // First check to see if its just the table we're observing directly.
    val tablesSelected = select.selectStmtList.flatMap {
      it.joinClause?.tableOrSubqueryList?.mapNotNull { tableOrSubquery ->
        val resolvedTable = tableOrSubquery.tableName?.reference?.resolve() ?: return@mapNotNull null
        PsiTreeUtil.getParentOfType(resolvedTable, Queryable::class.java)?.tableExposed()
      }.orEmpty()
    }
    tablesSelected.forEach {
      if (it.query.columns.flattenCompounded() == pureColumns) {
        val table = it.query.table
        if (table is SqlViewName) {
          // check, if this view uses exactly 1 pure table and use this table, if found.
          val createViewStmt = table.nameIdentifier?.parentOfType<SqlCreateViewStmt>()?.compoundSelectStmt
          if (createViewStmt != null) {
            val foundPureTable = SelectQueryable(createViewStmt).pureTable
            if (foundPureTable != null) {
              return@lazy foundPureTable
            }
          }
        }
        return@lazy it.tableName
      }
    }

    return@lazy select.tablesAvailable(select).firstOrNull {
      (it.tableName.parent !is SqlCteTableName) &&
        it.query.columns.flattenCompounded() == pureColumns
    }?.tableName
  }
}
