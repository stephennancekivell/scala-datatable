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
package com.github.martincooper.datatable.DataColumnSpecs

import com.github.martincooper.datatable.DataColumn
import org.scalatest._

class DataColumnTypeCheckSpec extends FlatSpec with Matchers {

  "A Data Column" should "allow add data of correct type" in {
    val originalColumn = new DataColumn[Long]("TestCol", (0L to 4L) map { i =>
      i
    })

    val result = originalColumn.add(99L)
    result.isSuccess should be(true)
    result.get.data should be(Seq(0L, 1L, 2L, 3L, 4L, 99L))
  }

  it should "not allow add data of invalid type" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i =>
      i
    })

    val result = originalColumn.add("String Value")
    result.isSuccess should be(false)
    result.failed.get.getMessage should be("Invalid value type on add.")
  }

  it should "allow insert data of correct type" in {
    val originalColumn = new DataColumn[Long]("TestCol", (0L to 4L) map { i =>
      i
    })

    val result = originalColumn.insert(2, 99L)
    result.isSuccess should be(true)
    result.get.data should be(Seq(0L, 1L, 99L, 2L, 3L, 4L))
  }

  it should "not allow insert data of invalid type" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i =>
      i
    })

    val result = originalColumn.insert(2, "String Value")
    result.isSuccess should be(false)
    result.failed.get.getMessage should be("Invalid value type on insert.")
  }

  it should "allow replace data of correct type" in {
    val originalColumn = new DataColumn[Long]("TestCol", (0L to 4L) map { i =>
      i
    })

    val result = originalColumn.replace(2, 99L)
    result.isSuccess should be(true)
    result.get.data should be(Seq(0L, 1L, 99L, 3L, 4L))
  }

  it should "not allow replace data of invalid type" in {
    val originalColumn = new DataColumn[Int]("TestCol", (0 to 4) map { i =>
      i
    })

    val result = originalColumn.replace(2, "String Value")
    result.isSuccess should be(false)
    result.failed.get.getMessage should be("Invalid value type on replace.")
  }
}
