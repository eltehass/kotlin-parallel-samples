package labWorks.lab6

import mpi.MPI

fun main(args: Array<String>) {
    MPI.Init(args)
    val me = MPI.COMM_WORLD.Rank()
    val size = MPI.COMM_WORLD.Size()
    println("Hi from <$me>")
    MPI.Finalize()
}