package problemtype

import dlx.DLX

/** Class to compute Exact cover for a set given the contender sets.
  *
  * [[https://en.wikipedia.org/wiki/Exact_cover Exact Cover]]
  *
  * @author Vishwas B Sharma (sharma.vishwas88@gmail.com)
  *
  * @param X set to cover
  * @param S collection of subsets of set X
  * @tparam T type of elements present in set X
  */
class ExactCover[T](X: Array[T], S: Map[String, Array[T]]) {

  /** Method to compute the exact cover for the given set.
    *
    * @return Key array of rows to pick.
    */
  def solve(): Array[String] = {
    val bitmapMatrix = ExactCover.mapInput(X, S)
    val myDLX = new DLX(bitmapMatrix)
    val solution = myDLX.solve()
    ExactCover.mapOutput(X, S, solution)
  }

  /** Method to compute the exact cover for the given set and print the solution. */
  def solveAndPrint() = {
    val resultSet = solve()
    println(resultSet.mkString(" "))
  }
}

object ExactCover {

  import util.MapperUtil._

  /**
    * Create a bitmap matrix for the given sets using the universe as the reference.
    *
    * @param universe The set to cover completely.
    * @param sets Contender sets which should be used to cover the universe.
    * @tparam T Type information about the elements of the universe.
    * @return bitmapMatrix where 1 in the matrix represents presence of the corresponding element
    *         from the Universal set in that particular row.
    */
  def mapInput[T](universe: Array[T], sets: Map[String, Array[T]]) = {
    if (universe.distinct.deep != universe.deep)
      throw new IllegalArgumentException("Universe should not contain duplicates")
    if (sets.keys.toArray.distinct.deep != sets.keys.toArray.deep)
      throw new IllegalArgumentException("Set keys should not contain duplicates")

    var bitmapMatrix = Array[Array[Byte]]()
    sets.keys.toList.sorted.zipWithIndex.foreach(item => {
      bitmapMatrix :+= bitmap[T](universe, sets(item._1))
    })

    bitmapMatrix
  }

  /** Maps the solution to easily readable output.
    *
    * @param universe set to cover
    * @param sets contender sets used to cover the universe set
    * @param solution provided by AlgorithmX
    * @tparam T type of elements in the universe array
    * @return Key array of rows to pick.
    */
  def mapOutput[T](universe: Array[T], sets: Map[String, Array[T]],
                   solution: Array[Array[Int]]): Array[String] = {
    val setsReverseMap = sets.map(_.swap)
    var result = Array[String]()
    solution.foreach(indexRow => {
      val elementRow = getSubArrayAtIndices(universe, indexRow)
      setsReverseMap.foreach(entry => {
        if (entry._1.sameElements(elementRow))
          result :+= entry._2
      })
    })
    result.sorted
  }
}
