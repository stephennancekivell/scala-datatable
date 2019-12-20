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

import com.github.martincooper.datatable.DataSort.TableSort

import scala.util.{ Failure, Try, Success }

/** Trait specifying common functionality between DataTable and DataView. */
trait BaseTable extends IndexedSeq[DataRow] with TableSort {
  val name: String
  val columns: DataColumnCollection
  val rows: DataRowCollection
  val table: DataTable
  val rowCount: Int

  def toDataView: DataView
  def toDataTable: DataTable
}

/** DataTable class. Handles the immutable storage and access of data in a Row / Column format. */
class DataTable private (tableName: String,
  dataColumns: Iterable[GenericColumn])
    extends BaseTable {

  val name = tableName
  val table: DataTable = this
  val columns = DataColumnCollection(this, dataColumns)
  val rowCount = length
  val rows = DataRowCollection(this)

  override def length: Int = {
    columns.length match {
      case 0 => 0
      case _ => columns.head.data.length
    }
  }

  override def apply(idx: Int): DataRow = DataRow(this, idx).get

  /** Outputs a more detailed toString implementation. */
  override def toString() = {
    val tableDetails = "DataTable:" + name + "[Rows:" + rowCount + "]"
    val colDetails = columns.map(col => "[" + col.toString + "]").mkString(" ")

    tableDetails + colDetails
  }

  /** Return a new DataView based on this table. */
  def toDataView: DataView = DataView(this).get

  /** Return a new DataTable based on this table (clone). */
  def toDataTable: DataTable = DataTable(name, columns).get
}

object DataTable {

  /** Builds an empty DataTable. */
  def apply(tableName: String): Try[DataTable] = {
    Success(new DataTable(tableName, Seq()))
  }

  /** Validates columns and builds a new DataTable. */
  def apply(tableName: String,
    columns: Iterable[GenericColumn]): Try[DataTable] = {
    validateDataColumns(columns) map { _ =>
      new DataTable(tableName, columns)
    }
  }

  def validateDataColumns(columns: Iterable[GenericColumn]): Try[Unit] = {
    val colSeq = columns.toSeq

    for {
      validLength <- validateColumnDataLength(colSeq)
      validNames <- validateDistinctColumnNames(colSeq)
    } yield validNames
  }

  /** Check all columns have the same number of rows. */
  private def validateColumnDataLength(
    columns: Seq[GenericColumn]): Try[Unit] = {
    columns.groupBy(_.data.length).toSeq.length > 1 match {
      case true => Failure(DataTableException("Columns have uneven row count."))
      case _ => Success(())
    }
  }

  /** Check all columns have distinct names. */
  private def validateDistinctColumnNames(
    columns: Seq[GenericColumn]): Try[Unit] = {
    columns.groupBy(_.name).toSeq.length != columns.length match {
      case true =>
        Failure(DataTableException("Columns contain duplicate names."))
      case _ => Success(())
    }
  }
}
