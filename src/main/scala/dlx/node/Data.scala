package dlx.node

/** Node representing Data.
  *
  * @author Vishwas B Sharma (sharma.vishwas88@gmail.com)
  *
  * @param L Points to cell on the left of current cell
  * @param R Points to cell on the right of current cell
  * @param U Points to cell above the current cell
  * @param D Points to cell below the current cell
  * @param C Points to column header
  */
class Data(var L: Data, var R: Data, var U: Data, var D: Data, var C: Data) {
  def this() = this(null, null, null, null, null)
}