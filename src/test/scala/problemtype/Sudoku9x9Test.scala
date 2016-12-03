package problemtype

import org.scalatest.FunSuite

/** Test to check correctness of Sudoku9x9 solution. */
class Sudoku9x9Test extends FunSuite {

  test("should solve the puzzle correctly") {
    val grid = Array[Array[Byte]](
      Array[Byte](0,0,0,4,0,0,0,0,2),
      Array[Byte](0,0,0,0,0,5,3,8,0),
      Array[Byte](0,7,0,0,0,0,0,9,6),
      Array[Byte](0,0,8,0,4,0,0,0,0),
      Array[Byte](0,9,0,2,8,6,0,4,0),
      Array[Byte](0,0,0,0,1,0,7,0,0),
      Array[Byte](2,1,0,0,0,0,0,6,0),
      Array[Byte](0,8,5,7,0,0,0,0,0),
      Array[Byte](4,0,0,0,0,1,0,0,0))

    val sudokuSolver = new Sudoku9x9(grid)
    // Pre computed solution to the puzzle created by hand.
    val expectedSolution = Array[Array[Int]](
      Array(8,3,6,4,7,9,1,5,2),
      Array(9,4,1,6,2,5,3,8,7),
      Array(5,7,2,1,3,8,4,9,6),
      Array(1,2,8,5,4,7,6,3,9),
      Array(7,9,3,2,8,6,5,4,1),
      Array(6,5,4,9,1,3,7,2,8),
      Array(2,1,7,3,9,4,8,6,5),
      Array(3,8,5,7,6,2,9,1,4),
      Array(4,6,9,8,5,1,2,7,3))
    // Expect the program to give the same solution as computed by hand.
    val solvedGrid = sudokuSolver.solve()
    for (i <- 0 until 9) {
      assert(solvedGrid(i).sameElements(expectedSolution(i)))
    }
  }
}
