package util

/** Object to provide utility functions helpful in mapping inputs and outputs.
  *
  * @author Vishwas B Sharma (sharma.vishwas88@gmail.com)
  */
object MapperUtil {

  /** Method to check if the value is present in the array or not.
    * Helpful when the value to search for is itself an array.
    *
    * @param arr to search the value in.
    * @param value to search for.
    * @return true if value found in array else false
    */
  def deepFind(arr: AnyRef, value: AnyRef): Boolean = {
    arr match {
      case arr: Array[Array[_]] =>
        arr.find(_.deep == value.asInstanceOf[Array[_]].deep) != None
      case _ => false
    }
  }

  /** Creates bitmap of the given array with respect to reference array.
    * Ex: If reference array is [1, 2, 3, 4] then
    * Bitmap of [1, 2] is [1, 1, 0, 0]
    * Bitmap of [1, 4] is [1, 0, 0, 1]
    *
    * @param refArray Reference to create the bitmap.
    * @param myArray Array to create the bitmap for.
    * @tparam T Type information.
    * @return Bitmap of myArray with respect to reference array.
    */
  def bitmap[T](refArray: Array[T], myArray: Array[T]): Array[Byte] = {
    if (refArray.length < myArray.length)
      throw new IllegalArgumentException("Reference array should be >= array to map in length.")
    if (refArray.distinct.deep != refArray.deep)
      throw new IllegalArgumentException("Reference array should not contain duplicates")

    val map = Array.fill[Byte](refArray.length)(0)
    for (i <- 0 to refArray.length - 1) {
      if (myArray contains refArray(i))
        map(i) = 1
      else if (deepFind(myArray.asInstanceOf[AnyRef], refArray(i).asInstanceOf[AnyRef]))
        map(i) = 1
    }
    map
  }

  /** Method to fetch subarray at indices from a reference array.
    *
    * @param refArray is the array to create subarray from.
    * @param indexArray provides the indices to fetch.
    * @tparam T type of elements in reference array.
    * @return Sub array of elements from refArray
    */
  def getSubArrayAtIndices[T](refArray: Array[T], indexArray: Array[Int]) = {
    refArray.zipWithIndex.collect({ case (x, i) if indexArray contains i => x })
  }
}
