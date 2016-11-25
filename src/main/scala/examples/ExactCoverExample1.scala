package examples

import problemtype.ExactCover

object ExactCoverExample1 extends App {
  val U: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
  val S: Map[String, Array[Int]] = Map[String, Array[Int]](
    "A" -> Array(3, 5, 6, 8),
    "B" -> Array(1, 4, 7),
    "C" -> Array(2, 3, 6),
    "D" -> Array(1, 4),
    "E" -> Array(2, 7),
    "F" -> Array(4, 5, 7),
    "G" -> Array(8, 9),
    "H" -> Array(3, 5, 6)
  )

  val exactCover: ExactCover[Int] = new ExactCover[Int](U, S)
  exactCover.solve()
}