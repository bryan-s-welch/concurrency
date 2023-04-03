import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class concurrency {

    static int ARRAY_SIZE = 200000000;
    static int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {

        int[] numbers = randomNumbers(ARRAY_SIZE);

        long parallelStart = System.currentTimeMillis();
        long parallelSum = parallel(numbers);
        long parallelEnd = System.currentTimeMillis();

        long singleStart = System.currentTimeMillis();
        long singleSum = singleThreadSum(numbers);
        long singleEnd = System.currentTimeMillis();

        System.out.println("sum of parallel: " + parallelSum + " |  time to process: " + (parallelEnd - parallelStart) + "ms");
        System.out.println("sum of single: " + singleSum + " | time to process: " + (singleEnd - singleStart) + "ms");
    }

    private static int[] randomNumbers(int size) {

        Random random = new Random();
        int[] numbers = new int[size];

        for (int i = 0; i < size; i++) {
            numbers[i] = random.nextInt(10) + 1;
        }
        return numbers;
    }

    private static long parallel(int[] numbers) {
        AtomicLong sum = new AtomicLong();
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                long threadSum = 0;
                for (int number : numbers) {
                    threadSum += number;
                }
                sum.addAndGet(threadSum);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return sum.get() / NUM_THREADS;
    }

    private static long singleThreadSum(int[] numbers) {

        long sum = 0;

        for (int number : numbers) {
            sum += number;
        }
        return sum;
    }
}
