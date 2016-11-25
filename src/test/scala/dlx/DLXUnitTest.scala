package dlx

import dlx.node.Column
import org.scalatest.FunSuite

/** Unit tests DLX class */
class DLXUnitTest extends FunSuite {

  test("DLX object should have same dimensions as the input with all 1s") {
    val input = Array(Array[Byte](1, 1, 1), Array[Byte](1, 1, 1), Array[Byte](1, 1, 1))
    val myDLX = new DLX(input)

    val myPtr = myDLX.head.R

    var (colPtr, colCount) = (myPtr, 0)
    while (colPtr.R != myPtr) {
      colCount += 1
      colPtr = colPtr.R.asInstanceOf[Column]
    }
    assert(colCount === 3, "Column count doesn't match")

    var (rowPtr, rowCount) = (myPtr, 0)
    while (rowPtr.D != myPtr) {
      rowCount += 1
      rowPtr = rowPtr.D
    }
    assert(rowCount === 3, "Row count doesn't match")
  }

  test("DLX object should count the 1s correctly") {
    val input = Array(Array[Byte](1, 1, 1), Array[Byte](0, 1, 1), Array[Byte](0, 0, 1))
    val myDLX = new DLX(input)

    var myPtr = myDLX.head.R

    for (i <- 1 to 3) {
      assert(myPtr.asInstanceOf[Column].S === i)
      myPtr = myPtr.R
    }
  }

  test("DLX object should cover and uncover columns properly") {
    val input = Array(Array[Byte](1, 1, 1), Array[Byte](0, 1, 1), Array[Byte](1, 1, 0))
    val myDLX = new DLX(input)

    // Column count should be 2, 3, 2
    assert(myDLX.head.R.asInstanceOf[Column].S === 2)
    assert(myDLX.head.R.R.asInstanceOf[Column].S === 3)
    assert(myDLX.head.R.R.R.asInstanceOf[Column].S === 2)
    assert(myDLX.head.R.R.R.R.asInstanceOf[Column].S === 0) // head

    // Fetch column 1
    val col1: Column = myDLX.head.R.asInstanceOf[Column]

    // Covering column 1 should make column counts 1, 1
    myDLX.coverColumn(col1)
    // The first column would have been removed
    assert(myDLX.head.R.asInstanceOf[Column].S === 1)
    assert(myDLX.head.R.R.asInstanceOf[Column].S === 1)
    assert(myDLX.head.R.R.R.asInstanceOf[Column].S === 0) // head

    // Uncovering column 1 should leave the column count back at 2, 3, 2
    myDLX.uncoverColumn(col1)
    // The first column would have been added back
    assert(myDLX.head.R.asInstanceOf[Column].S === 2)
    assert(myDLX.head.R.R.asInstanceOf[Column].S === 3)
    assert(myDLX.head.R.R.R.asInstanceOf[Column].S === 2)
    assert(myDLX.head.R.R.R.R.asInstanceOf[Column].S === 0) // head

    // Fetch column 2
    val col2: Column = myDLX.head.R.R.asInstanceOf[Column]

    // Covering column 2 should make column counts 0, 0
    myDLX.coverColumn(col2)
    // The second column would have been removed
    assert(myDLX.head.R.asInstanceOf[Column].S === 0)
    assert(myDLX.head.R.R.asInstanceOf[Column].S === 0)
    assert(myDLX.head.R.R.R.asInstanceOf[Column].S === 0) // head

    // Uncovering column 2 should leave the column count back at 2, 3, 2
    myDLX.uncoverColumn(col2)
    // The second column would have been added back
    assert(myDLX.head.R.asInstanceOf[Column].S === 2)
    assert(myDLX.head.R.R.asInstanceOf[Column].S === 3)
    assert(myDLX.head.R.R.R.asInstanceOf[Column].S === 2)
    assert(myDLX.head.R.R.R.R.asInstanceOf[Column].S === 0) // head

    // Fetch column 3
    val col3: Column = myDLX.head.R.R.R.asInstanceOf[Column]

    // Covering column 3 should make column counts 1, 1
    myDLX.coverColumn(col3)
    // The third column would have been removed
    assert(myDLX.head.R.asInstanceOf[Column].S === 1)
    assert(myDLX.head.R.R.asInstanceOf[Column].S === 1)
    assert(myDLX.head.R.R.R.asInstanceOf[Column].S === 0) // head

    // Uncovering column 3 should leave the column count back at 2, 3, 2
    myDLX.uncoverColumn(col3)
    // The third column would have been added back
    assert(myDLX.head.R.asInstanceOf[Column].S === 2)
    assert(myDLX.head.R.R.asInstanceOf[Column].S === 3)
    assert(myDLX.head.R.R.R.asInstanceOf[Column].S === 2)
    assert(myDLX.head.R.R.R.R.asInstanceOf[Column].S === 0) // head
  }

  test("DLX object should column cover combinations should be reversible") {
    val input = Array(Array[Byte](1, 1, 1), Array[Byte](0, 1, 1), Array[Byte](1, 1, 0))
    val myDLX = new DLX(input)

    // Column count should be 2, 3, 2
    assert(myDLX.head.R.asInstanceOf[Column].S === 2)
    assert(myDLX.head.R.R.asInstanceOf[Column].S === 3)
    assert(myDLX.head.R.R.R.asInstanceOf[Column].S === 2)
    assert(myDLX.head.R.R.R.R.asInstanceOf[Column].S === 0) // head

    // Fetch column 1
    val col1: Column = myDLX.head.R.asInstanceOf[Column]
    // Fetch column 2
    val col2: Column = myDLX.head.R.R.asInstanceOf[Column]

    myDLX.coverColumn(col1)
    myDLX.coverColumn(col2)
    // Reversing should be done in the reverse order of covering
    myDLX.uncoverColumn(col2)
    myDLX.uncoverColumn(col1)

    // This should leave back the DLX object in the same state at the beginning.
    // Column count should be 2, 3, 2
    assert(myDLX.head.R.asInstanceOf[Column].S === 2)
    assert(myDLX.head.R.R.asInstanceOf[Column].S === 3)
    assert(myDLX.head.R.R.R.asInstanceOf[Column].S === 2)
    assert(myDLX.head.R.R.R.R.asInstanceOf[Column].S === 0) // head
  }
}
