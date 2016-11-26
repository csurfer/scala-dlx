package dlx

import dlx.node.{Data, Column}

import scala.collection.mutable.ListBuffer

/** Class implementing Algorithm X using Dancing Links data structure as described in Dancing Links
  * paper by Prof. Donald Knuth.
  *
  * Reference : [[https://www.ocf.berkeley.edu/~jchu/publicportal/sudoku/0011047.pdf Dancing Links]]
  *
  * @author Vishwas B Sharma (sharma.vishwas88@gmail.com)
  *
  * @param input Byte array representing the input
  */
class DLX(var input: Array[Array[Byte]]) {
  // Validate input array.
  if (input.isEmpty || input(0).isEmpty) {
    throw new IllegalArgumentException("Invalid input array provided.")
  }

  // Initialize head.
  val head: Column = new Column(0, -1)
  head.L = head
  head.R = head
  head.U = head
  head.D = head

  // Initialize column headers.
  for (colIndex <- input(0).indices) {
    val col: Column = new Column(0, colIndex)
    col.L = head.L
    col.R = head
    col.U = col
    col.D = col
    head.L.R = col
    head.L = col
  }

  // Initialize the sparse matrix.
  for (rowIndex <- input.indices) {
    var (curCol, dataRow) = (head.R, Array[Data]())

    // Create data row and initialize vertical linking.
    for (colIndex <- input(rowIndex).indices) {
      if (input(rowIndex)(colIndex) != 0) {
        val data = new Data
        // Link data cell
        data.L = data
        data.R = data
        data.U = curCol.U
        data.D = curCol
        data.C = curCol
        curCol.U.D = data
        curCol.U = data

        // Book keeping
        curCol.asInstanceOf[Column].S += 1
        dataRow :+= data
      }
      curCol = curCol.R
    }

    // Create horizontal linking for the data row.
    val rowHead = dataRow(0)
    for (colIndex <- 1 until dataRow.length) {
      dataRow(colIndex).L = rowHead.L
      dataRow(colIndex).R = rowHead
      rowHead.L.R = dataRow(colIndex)
      rowHead.L = dataRow(colIndex)
    }
  }



  /** Column cover operation as described in the paper this class implementation is based on.
    *
    * @param col to cover.
    */
  def coverColumn(col: Column): Unit = {
    // Unlink the column head from the header row
    col.L.R = col.R
    col.R.L = col.L

    // Move down the column covering each cell
    var myRow = col.D
    while (myRow != col) {
      var myCol = myRow.R
      while (myCol != myRow) {
        // Vertically unlink the cell from its column
        myCol.U.D = myCol.D
        myCol.D.U = myCol.U
        // Update its column header.
        myCol.C.asInstanceOf[Column].S -= 1

        // Move to the next column with a cell in the current row
        myCol = myCol.R
      }

      // Move to the next row with a cell in the current column
      myRow = myRow.D
    }
  }

  /** Column uncover operation as described in the paper this class implementation is based on.
    *
    * @param col to uncover.
    */
  def uncoverColumn(col: Column): Unit = {
    // Move up the column uncovering each cell
    var myRow = col.U
    while (myRow != col) {
      var myCol = myRow.L
      while (myCol != myRow) {
        // Vertically link back the cell to its column
        myCol.U.D = myCol
        myCol.D.U = myCol
        // Update its column header.
        myCol.C.asInstanceOf[Column].S += 1

        // Move to the previous column with a cell in the current row
        myCol = myCol.L
      }

      // Move to the previous row with a cell in the current column
      myRow = myRow.U
    }

    // Link the column head back into its header row
    col.L.R = col
    col.R.L = col
  }

  /** Choose column with lowest number of 1s.
    *
    * @return column with lowest number of 1s
    */
  private def chooseColumn(): Column = {
    var (chCol, myCol) = (this.head.R, this.head.R)

    // Loop through the headers trying to find the column with lowest number of 1s in it.
    while (myCol != this.head) {
      chCol = if (myCol.asInstanceOf[Column].S < chCol.asInstanceOf[Column].S) myCol else chCol
      myCol = myCol.R
    }

    chCol.asInstanceOf[Column]
  }

  /** Method to check if the DLX structure is empty.
    *
    * @return true if empty else false
    */
  private def isEmpty(): Boolean = this.head.R == this.head

  /** Method to search through the input to find a viable solution.
    *
    * @param curOrder is the order of rows being considered for solution
    * @param solution is the order which suggests the search has reached a viable endpoint
    */
  private def search(curOrder: ListBuffer[Data], solution: ListBuffer[Data]): Unit = {
    if (this.isEmpty) {
      // A solution is found.
      curOrder.copyToBuffer(solution)
    }

    val chCol = this.chooseColumn()
    if (chCol.asInstanceOf[Column].S == 0) {
      return
    }

    this.coverColumn(chCol.asInstanceOf[Column])

    var myRow = chCol.D

    while (myRow != chCol) {
      curOrder += myRow

      var myCol = myRow.R

      while (myCol != myRow) {
        this.coverColumn(myCol.C.asInstanceOf[Column])
        myCol = myCol.R
      }

      this.search(curOrder, solution)

      myCol = myRow.L

      while (myCol != myRow) {
        this.uncoverColumn(myCol.C.asInstanceOf[Column])
        myCol = myCol.L
      }

      curOrder -= myRow

      myRow = myRow.D
    }

    this.uncoverColumn(chCol.asInstanceOf[Column])
  }

  /** Method to convert solution presented as list of nodes to list of indexes.
    *
    * @param solution order of nodes to be picked.
    */
  private def getSolutionAsIndexArrays(solution: ListBuffer[Data]): Array[Array[Int]] = {
    var solutionAsIndexLists = Array[Array[Int]]()
    solution.foreach(row => {
      var myCol = row
      var flag = true
      var indexRow = Array[Int]()
      while (flag) {
        indexRow :+= myCol.C.asInstanceOf[Column].N
        if (myCol.R == row)
          flag = false
        else
          myCol = myCol.R
      }
      solutionAsIndexLists :+= indexRow
    })
    solutionAsIndexLists
  }

  /** Method to search through the possibilities using the DLX structure to return exact cover. */
  def solve(): Array[Array[Int]] = {
    val curOrder: ListBuffer[Data] = new ListBuffer[Data]()
    val solution: ListBuffer[Data] = new ListBuffer[Data]()

    this.search(curOrder, solution)

    return this.getSolutionAsIndexArrays(solution)
  }
}