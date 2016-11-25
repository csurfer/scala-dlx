package dlx.node

/** Node representing Column header.
  *
  * @author Vishwas B Sharma (sharma.vishwas88@gmail.com)
  *
  * @param S Number of 1s in the column
  * @param N Name identifier for the column
  * @param L Points to cell on the left of current cell
  * @param R Points to cell on the right of current cell
  * @param U Points to cell above the current cell
  * @param D Points to cell below the current cell
  * @param C Points to column header
  */
class Column(var S: Int, var N: Byte, L: Data, R: Data, U: Data, D: Data, C:Data)
    extends Data(L, R, U, D, C) {
  def this() = this(0, -1, null, null, null, null, null)
  def this(S: Int) = this(S, -1, null, null, null, null, null)
  def this(S: Int, N: Byte) = this(S, N, null, null, null, null, null)
}
