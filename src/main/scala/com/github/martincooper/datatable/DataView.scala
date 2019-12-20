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
package com.github.martincooper.datatable

import scala.collection.mutable
import scala.util.{ Failure, Success, Try }

/** Provides a view over a DataTable to store filtered data sets. */
class DataView private (dataTable: DataTable, dataRows: Iterable[DataRow])
    extends BaseTable {

  val table = dataTable
  val rows = DataRowCollection(table, dataRows)
  val name = table.name
  val rowCount = rows.length
  val columns = table.columns

  override def length: Int = rows.length

  override def apply(idx: Int): DataRow = rows(idx)

  /** Outputs a more detailed toString implementation. */
  override def toString() = {
    val tableDetails = "DataView:" + name + "[Rows:" + rowCount + "]"
    val colDetails = columns.map(col => "[" + col.toString + "]").mkString(" ")

    tableDetails + colDetails
  }

  /** Return a new DataView based on this DataView (clone). */
  def toDataView: DataView = DataView(table, rows).get

  /** Return a new DataTable based on this DataView. */
  def toDataTable: DataTable = DataView.toDataTable(this).get
}

object DataView {

  /** Builder for a new DataView. */
  def newBuilder(dataTable: DataTable): mutable.Builder[DataRow, DataView] =
    Vector
      .newBuilder[DataRow] mapResult (vector => DataView(dataTable, vector).get)

  def apply(sourceDataTable: DataTable): Try[DataView] = {
    Success(new DataView(sourceDataTable, sourceDataTable.rows))
  }

  def apply(sourceDataTable: DataTable,
    dataRows: Iterable[DataRow]): Try[DataView] = {
    val indexedData = dataRows.toIndexedSeq

    validateDataRows(sourceDataTable, indexedData) match {
      case Success(_) => Success(new DataView(sourceDataTable, indexedData))
      case Failure(ex) => Failure(ex)
    }
  }

  /** Builds a new DataTable from a DataView. */
  def toDataTable(dataView: DataView): Try[DataTable] = {
    val rowIndexes = dataView.map(row => row.rowIndex)
    val newColumns = dataView.columns.map(col => col.buildFromRows(rowIndexes))

    Try(newColumns.map(_.get)) match {
      case Failure(ex) => Failure(ex)
      case Success(cols) => DataTable(dataView.name, cols)
    }
  }

  /** Validate all data rows belong to the same DataTable to ensure data integrity. */
  def validateDataRows(sourceDataTable: DataTable,
    dataRows: Iterable[DataRow]): Try[Unit] = {
    val indexedData = dataRows.toIndexedSeq

    indexedData.forall(row => row.table eq sourceDataTable) match {
      case false =>
        Failure(
          DataTableException(
            "DataRows do not all belong to the specified table."
          )
        )
      case true => Success(())
    }
  }
}
