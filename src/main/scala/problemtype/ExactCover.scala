package problemtype

import dlx.DLX

class ExactCover[T](U: Array[T], S: Map[String, Array[T]]) {

  def solve() = {
    val bitmapMatrix = ExactCover.mapInput(U, S)
    val myDLX = new DLX(bitmapMatrix)
    val solution = myDLX.solve()
    ExactCover.mapOutput(U, S, solution)
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
    */
  def mapOutput[T](universe: Array[T], sets: Map[String, Array[T]],
                   solution: Array[Array[Byte]]) = {
    val setsReverseMap = sets.map(_.swap)
    solution.foreach(indexRow => {
      val elementRow = getSubArrayAtIndices(universe, indexRow)
      setsReverseMap.foreach(entry => {
        if (entry._1.sameElements(elementRow))
          print(entry._2 + " ")
      })
    })
  }
}
