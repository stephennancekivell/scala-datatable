/**
 * Copyright 2014-2015 Martin Cooper
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.martincooper.datatable.DataSort

import com.github.martincooper.datatable.{ DataRow, DataRowCollection, DataTable, DataView }
import com.github.martincooper.datatable.DataSort.SortEnum.{ Ascending, SortOrder }

import scala.util.Try

/**
 * TableSort Trait.
 * Adds Quick Sort methods onto the DataTable.
 */
trait TableSort {

  val rows: DataRowCollection
  val table: DataTable

  /** Performs a quick sort of the DataTable, returning a sorted DataView. */
  def quickSort(columnName: String): Try[DataView] = {
    quickSort(columnName, Ascending)
  }

  /** Performs a quick sort of the DataTable, returning a sorted DataView. */
  def quickSort(columnName: String, sortOrder: SortOrder): Try[DataView] = {
    val drows: Iterable[DataRow] = rows
    val si: Iterable[SortItem] = Seq(SortItem(columnName, sortOrder))

    DataSort.quickSort(table, drows, si)
  }

  /** Performs a quick sort of the DataTable, returning a sorted DataView. */
  def quickSort(columnIndex: Int): Try[DataView] = {
    quickSort(columnIndex, Ascending)
  }

  /** Performs a quick sort of the DataTable, returning a sorted DataView. */
  def quickSort(columnIndex: Int, sortOrder: SortOrder): Try[DataView] = {
    DataSort.quickSort(table, rows, Seq(SortItem(columnIndex, sortOrder)))
  }

  /** Performs a quick sort of the DataTable, returning a sorted DataView. */
  def quickSort(sortItem: SortItem): Try[DataView] = {
    DataSort.quickSort(table, rows, Seq(sortItem))
  }

  /** Performs a quick sort of the DataTable, returning a sorted DataView. */
  def quickSort(sortItems: Iterable[SortItem]): Try[DataView] = {
    DataSort.quickSort(table, rows, sortItems)
  }
}
