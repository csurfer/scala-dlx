# scala-dlx
**Dancing links(DLX)** based implementation of **AlgorithmX** in **Scala** to solve **Exact Cover** based problems.

## References
- [Prof. Donald Knuth's paper called Dancing Links](https://www.ocf.berkeley.edu/~jchu/publicportal/sudoku/0011047.pdf)
- [AlgorithmX](https://en.wikipedia.org/wiki/Knuth's_Algorithm_X)

## Links to different parts of the project
- [Examples](src/main/scala/examples) of different problems. **(Start here to get a feel for the problem at hand)**
- [DLX core](src/main/scala/dlx) consists of implementation of classes requried to implement DLX structure, and the implementaion of AlgorithmX using DLX.
- [Problem Types](src/main/scala/problemtype) consists of different problems we can hope to solve using AlgorithmX.
- [Utility](src/main/scala/util) functions.

## Currently implemented solutions
- Set exact cover
- 9x9 Sudoku solver
