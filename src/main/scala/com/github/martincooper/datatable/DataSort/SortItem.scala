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

import com.github.martincooper.datatable.DataSort.SortEnum.{
  Ascending,
  SortOrder
}
import com.github.martincooper.datatable.{ ItemByIndex, ItemByName, ItemIdentity }

object SortEnum {
  sealed trait SortOrder
  case object Ascending extends SortOrder
  case object Descending extends SortOrder
  val sortOrders = Seq(Ascending, Descending)
}

/** Defines a column and a corresponding sort order. */
case class SortItem(columnIdentity: ItemIdentity, order: SortOrder)

object SortItem {

  /** Builds an new SortOrder item by column name. */
  def apply(columnName: String): SortItem = {
    apply(columnName, Ascending)
  }

  /** Builds an new SortOrder item by column name. */
  def apply(columnName: String, sortOrder: SortOrder): SortItem = {
    new SortItem(new ItemByName(columnName), sortOrder)
  }

  /** Builds an new SortOrder item by column index. */
  def apply(columnIndex: Int): SortItem = {
    apply(columnIndex, Ascending)
  }

  /** Builds an new SortOrder item by column index. */
  def apply(columnIndex: Int, sortOrder: SortOrder): SortItem = {
    new SortItem(new ItemByIndex(columnIndex), sortOrder)
  }
}
