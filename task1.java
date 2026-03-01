import java.util.concurrent.*;
import java.util.*;

public class task1 {

    static class PrimeTask implements Callable<Integer> {
        private int start;
        private int end;

        public PrimeTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Integer call() {
            int count = 0;
            for (int i = start; i <= end; i++) {
                if (isPrime(i)) {
                    count++;
                }
            }
            return count;
        }

        private boolean isPrime(int n) {
            if (n <= 1) return false;
            if (n == 2) return true;
            if (n % 2 == 0) return false;

            for (int i = 3; i * i <= n; i += 2) {
                if (n % i == 0) return false;
            }
            return true;
        }
    }

    public static void main(String[] args) throws Exception {

        int maxNumber = 1_000_000;
        int numThreads = Runtime.getRuntime().availableProcessors();

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> results = new ArrayList<>();

        int chunkSize = maxNumber / numThreads;

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize + 1;
            int end = (i == numThreads - 1) ? maxNumber : (i + 1) * chunkSize;
            results.add(executor.submit(new PrimeTask(start, end)));
        }

        int totalPrimes = 0;
        for (Future<Integer> result : results) {
            totalPrimes += result.get();
        }

        long endTime = System.currentTimeMillis();

        executor.shutdown();

        System.out.println("Total prime numbers: " + totalPrimes);
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }
}