package problemtype

import dlx.DLX

/** Class to compute solution for a Sudoku.
  *
  * [[https://en.wikipedia.org/wiki/Sudoku Sudoku]]
  *
  * Each row represents a possibility. As each cell of a 9x9 Sudoku can be filled with 9 different
  * values we have a total of 9*9*9 i,e 729 different possibilities each represented by the rows.
  * Note that if a cell is filled then we already know the value that goes into that cell hence
  * the 9 possibilities for that cell reduces to just one. So we will have a maximum of 729 rows.
  *
  * Each column represent a constraint that the solution should respect. The constraints to follow
  * are
  * 1. Each cell should be filled with a value. => 9 * 9 = 81 cells
  * 2. Each row should have numbers from 1-9. => 9 rows * 9 values = 81
  * 3. Each column should have numbers from 1-9. => 9 cols * 9 values = 81
  * 4. Each grid should have numbers from 1-9. => 9 cells in each grid * 9 values = 81
  * In total 4*81 324 constraints represented as columns.
  *
  * Exact cover solution for the grid represented this way tells us which items to choose from the
  * possibilities hence solving the Sudoku grid.
  *
  * @author Vishwas B Sharma (sharma.vishwas88@gmail.com)
  *
  * @param grid 9x9 Sudoku grid to be solved
  */
class Sudoku9x9(grid: Array[Array[Byte]]) {

  /** Method to compute solution to the Sudoku grid given.
    *
    * @return Solved grid.
    */
  def solve(): Array[Array[Int]] = {
    if (grid.length != 9 || grid(0).length != 9) {
      throw new IllegalArgumentException("Array dimensions should be 9x9")
    }

    val bitmapMatrix = Sudoku9x9.mapInput(grid)
    val myDLX = new DLX(bitmapMatrix)
    val solution = myDLX.solve()
    Sudoku9x9.mapOutput(solution)
  }

  /** Method to compute solution to the Sudoku grid given and print it. */
  def solveAndPrint() = {
    val solvedGrid = solve()
    solvedGrid.foreach(row => println(row.mkString(" ")))
  }
}

object Sudoku9x9 {

  /** Total number of constraints to statisfy. */
  val TOTAL_CONSTRAINTS_TO_SATISFY: Int = 324

  /** First 81 columns help us verify the constraint that all cells in the grid have a value, i,e
    * we expect the chosen combination to `exact cover` these columns with 1.
    *
    * rowNumber * 9 + columnNumber translates the 2-D grid to 1-D.
    */
  val CELL_CONSTRAINT_OFFSET: Int = 0

  /** Second 81 columns help us verify the constraint that all values appear in a row once, i,e
    * we expect the chosen combination to `exact cover` these columns with 1.
    *
    * rowNumber * 9 points to the row we are talking about and a set bit in the next 9 elements
    * tells us what number we are choosing for this row.
    */
  val ROW_CONSTRAINT_OFFSET: Int = 81 * 1

  /** Third 81 columns help us verify the constraint that all values appear in a column once, i,e
    * we expect the chosen combination to `exact cover` these columns with 1.
    *
    * colNumber * 9 points to the column we are talking about and a set bit in the next 9 elements
    * tells us what number we are choosing for this column.
    */
  val COL_CONSTRAINT_OFFSET: Int = 81 * 2

  /** Fourth 81 columns help us verify the constraint that all values appear in a grid once, i,e
    * we expect the chosen combination to `exact cover` these columns with 1.
    *
    * Grids are aligned as follows in the Sudoku grid.
    *
    *    0 1 2
    *    3 4 5
    *    6 7 8
    *
    * where each number points to a 3x3 box. Row and Column number to grid number can be fetched by
    * formula 3 * (rowNumber // 3) + (colNumber // 3)
    */
  val GRID_CONSTRAINT_OFFSET: Int = 81 * 3

  /** Fetches the grid number for the given row and column.
    *
    * @param rowNumber of the cell
    * @param colNumber of the cell
    * @return grid number for the cell
    */
  private def getGrid(rowNumber: Int, colNumber: Int): Int = {
    3 * (rowNumber / 3) + (colNumber / 3)
  }

  /** Possibility is defined as a row of length TOTAL_CONSTRAINTS_TO_SATISFY with 0s and 1s, where
    * 1s tell what constraints are satisfied if the value (v) is at row (r) and column (c)
    *
    * @param r row number
    * @param c column number
    * @param v value to update in the cell
    * @return Possibility
    */
  private def getPossibility(r: Byte, c: Byte, v: Byte) = {
    // Appearance of a value (v) at row number (r), column number (c) dictates that it satisfies
    // 4 constraints.
    val currentPossibility = Array.ofDim[Byte](TOTAL_CONSTRAINTS_TO_SATISFY)

    // 1. Cell (r)(c) is filled.
    currentPossibility(r * 9 + c) = 1

    // 2. Row (r) has seen value (v)
    currentPossibility(ROW_CONSTRAINT_OFFSET + r * 9 + v) = 1

    // 3. Column (c) has seen value (v)
    currentPossibility(COL_CONSTRAINT_OFFSET + c * 9 + v) = 1

    // 4. Grid (g) has seen value (v).
    currentPossibility(GRID_CONSTRAINT_OFFSET + getGrid(r, c) * 9 + v) = 1

    // We return this as a possibility
    currentPossibility
  }

  /** Maps the given Sudoku grid into UpperBound(729x324) bitmap matrix representing the constraints
    * that need to be applied.
    *
    * @param grid representing input Sudoku
    * @return bitmapMatrix where 1 represents what constraints are satisfied if a particular row is
    *         chosen
    */
  def mapInput(grid: Array[Array[Byte]]) = {
    var bitmapMatrix = Array[Array[Byte]]()
    for (r <- grid.indices; c <- grid(0).indices) {
      if (grid(r)(c) != 0) {
        // if the cell already has a value then generate the only possibility and append
        val cellValue = grid(r)(c) - 1 // 1-9 to 0-8 translation
        bitmapMatrix :+= getPossibility(r.toByte, c.toByte, cellValue.toByte)
      } else {
        // if the cell doesn't have a value then generate all the possibilities and append
        for (v <- 0 to 8) {
          bitmapMatrix :+= getPossibility(r.toByte, c.toByte, v.toByte)
        }
      }
    }
    bitmapMatrix
  }

  /** Maps the solution to easily readable output.
    *
    * @param solution provided by AlgorithmX
    * @return Solved grid.
    */
  def mapOutput(solution: Array[Array[Int]]): Array[Array[Int]] = {
    val solvedGrid = Array.ofDim[Int](9, 9)
    solution.foreach(row => {
      val sortedRow = row.sorted
      val (r: Int, c: Int) = (sortedRow(0) / 9, sortedRow(0) % 9)
      val cellValue: Int = sortedRow(1) - ROW_CONSTRAINT_OFFSET - r * 9 + 1
      solvedGrid(r)(c) = cellValue
    })
    solvedGrid
  }
}
