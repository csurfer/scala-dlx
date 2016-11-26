package examples

import problemtype.Sudoku9x9

object Sudoku9x9Example0 extends App {
  val grid = Array[Array[Byte]](
      Array[Byte](0,0,0,2,6,0,7,0,1),
      Array[Byte](6,8,0,0,7,0,0,9,0),
      Array[Byte](1,9,0,0,0,4,5,0,0),
      Array[Byte](8,2,0,1,0,0,0,4,0),
      Array[Byte](0,0,4,6,0,2,9,0,0),
      Array[Byte](0,5,0,0,0,3,0,2,8),
      Array[Byte](0,0,9,3,0,0,0,7,4),
      Array[Byte](0,4,0,0,5,0,0,3,6),
      Array[Byte](7,0,3,0,1,8,0,0,0))

  val sudokuSolver = new Sudoku9x9(grid)
  sudokuSolver.solve()
}
