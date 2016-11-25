package util

import org.scalatest.FunSuite
import util.MapperUtil.bitmap

/** Unit tests MapperUtil class */
class MapperUtilUnitTest extends FunSuite {

  test("bitmap() should map array to reference array correctly") {
    assert(bitmap(Array(1, 2, 3, 4), Array(1, 3)) === Array(1, 0, 1, 0))
    assert(bitmap(Array(2, 4, 3, 1), Array(2, 3)) === Array(1, 0, 1, 0))
  }

  test("bitmap() should throw IllegalArgumentException if reference array length is lesser") {
    assertThrows[IllegalArgumentException](bitmap(Array(1, 2), Array(1, 2, 3)))
  }

  test("bitmap() should throw IllegalArgumentException if reference array contains duplicates") {
    assertThrows[IllegalArgumentException](bitmap(Array(1, 2, 3, 3), Array(1, 2)))
  }

  test("bitmap() should be able to handle different types graciously") {
    assert(bitmap[Int](Array(1, 2, 3, 4), Array(1, 3)) === Array(1, 0, 1, 0),
      "causing failure of Bitmap of Int Array")

    assert(bitmap[String](Array("1", "2", "3", "4"), Array("1", "3")) === Array(1, 0, 1, 0),
      "causing failure of Bitmap of String Array")

    assert(bitmap[Byte](Array[Byte](1, 2, 3, 4), Array[Byte](1, 3)) === Array(1, 0, 1, 0),
      "causing failure of Bitmap of Byte Array")

    // Bitmap of Array of Int Array
    val rIntArray = Array[Array[Int]](Array(1), Array(2), Array(3))
    val mIntArray = Array[Array[Int]](Array(1), Array(3))
    assert(bitmap[Array[Int]](rIntArray, mIntArray) === Array(1, 0, 1),
      "causing failure of Bitmap of Array of Int Array")

    // Bitmap of Array of String Array
    val rStringArray =
      Array[Array[String]](Array[String]("1"), Array[String]("2"), Array[String]("3"))
    val mStringArray =
      Array[Array[String]](Array[String]("1"), Array[String]("3"))
    assert(bitmap[Array[String]](rStringArray, mStringArray) === Array(1, 0, 1),
      "causing failure of Bitmap of Array of String Array")

    // Bitmap of Array of Byte Array
    val rByteArray = Array[Array[Byte]](Array[Byte](1), Array[Byte](2), Array[Byte](3))
    val mByteArray = Array[Array[Byte]](Array[Byte](1), Array[Byte](3))
    assert(bitmap[Array[Byte]](rByteArray, mByteArray) === Array(1, 0, 1),
      "causing failure of Bitmap of Array of Byte Array")
  }
}
