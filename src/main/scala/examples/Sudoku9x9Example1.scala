package examples

import problemtype.Sudoku9x9

object Sudoku9x9Example1 extends App {
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
  sudokuSolver.solveAndPrint()
}
