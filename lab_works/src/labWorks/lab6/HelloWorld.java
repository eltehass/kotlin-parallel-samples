package labWorks.lab6;

import mpi.MPI;

import java.util.Random;

/**
 * 3. Створити вектор з N >= 1000 елементами з випадкових чисел. Знайти максимальний та
 * мінімальний елементи векторів.
 **/

public class HelloWorld {

    private static final int ROOT_INDEX = 0;
    private static final int ARRAY_ELEMENTS_COUNT = 1000;
    private static final int ONE_NUMBER_BUFFER_SIZE = 1;

    public static void main(String[] args) {
        MPI.Init(args);
        var currentExecutorIndex = MPI.COMM_WORLD.Rank();
        var executorsCount = MPI.COMM_WORLD.Size();
        var localChunkSize = ARRAY_ELEMENTS_COUNT / executorsCount;

        // Numbers buffers
        var generalNumbersBuffer = new int[ARRAY_ELEMENTS_COUNT];
        var localNumbersBuffer = new int[localChunkSize];

        // Buffers for receiving min and max values
        var minNumberBuffer = new int[ONE_NUMBER_BUFFER_SIZE];
        var maxNumberBuffer = new int[ONE_NUMBER_BUFFER_SIZE];

        // Init array with random values on root executor
        if (currentExecutorIndex == ROOT_INDEX) {
            var random = new Random();
            for (int i = 0; i < generalNumbersBuffer.length; i++) {
                generalNumbersBuffer[i] = random.nextInt(1500) + 13;
                System.out.println("Number " + (i + 1) + " = " + generalNumbersBuffer[i]);
            }
        }

        // Sending data to local buffers
        MPI.COMM_WORLD.Scatter(generalNumbersBuffer, 0, localChunkSize, MPI.INT, localNumbersBuffer, 0, localChunkSize, MPI.INT, ROOT_INDEX);

        // Finding local min and max values
        var localMinValue = Integer.MAX_VALUE;
        var localMaxValue = Integer.MIN_VALUE;
        for (int value : localNumbersBuffer) {
            if (localMinValue > value) { localMinValue = value; }
            if (localMaxValue < value) { localMaxValue = value; }
        }

        // Saving min and max values from all executors to appropriate buffers
        MPI.COMM_WORLD.Reduce(new int[]{localMaxValue}, 0, maxNumberBuffer, 0, 1, MPI.INT, MPI.MAX, ROOT_INDEX);
        MPI.COMM_WORLD.Reduce(new int[]{localMinValue}, 0, minNumberBuffer, 0, 1, MPI.INT, MPI.MIN, ROOT_INDEX);

        if (currentExecutorIndex == ROOT_INDEX) {
            System.out.println("Max value = " + maxNumberBuffer[0]);
            System.out.println("Min value = " + minNumberBuffer[0]);
        }
    }
}