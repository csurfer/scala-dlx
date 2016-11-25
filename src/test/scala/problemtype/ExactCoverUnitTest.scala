package problemtype

import org.scalatest.FunSuite
import problemtype.ExactCover.mapInput

/** Unit tests ExactCover class */
class ExactCoverUnitTest extends FunSuite {

  test("mapInput() should map inputs correctly") {
    val universe: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7)
    val sets: Map[String, Array[Int]] = Map[String, Array[Int]](
      "A" -> Array(1, 4, 7),
      "B" -> Array(1, 4),
      "C" -> Array(4, 5, 7),
      "D" -> Array(3, 5, 6),
      "E" -> Array(2, 3, 6, 7),
      "F" -> Array(2, 7)
    )
    val expectedBitmapMatrix = Array(
      Array(1, 0, 0, 1, 0, 0, 1),
      Array(1, 0, 0, 1, 0, 0, 0),
      Array(0, 0, 0, 1, 1, 0, 1),
      Array(0, 0, 1, 0, 1, 1, 0),
      Array(0, 1, 1, 0, 0, 1, 1),
      Array(0, 1, 0, 0, 0, 0, 1)
    )

    val bitmapMatrix = mapInput(universe, sets)

    assert(bitmapMatrix === expectedBitmapMatrix, "making the bitmap produced incorrect.")
  }
}

