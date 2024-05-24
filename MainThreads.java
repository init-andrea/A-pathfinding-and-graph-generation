import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainThreads {
    public static void main(String[] args) {
        int numTests = 10000; // Number of random grids to test
        int rows = 100;
        int cols = 100;
        double obstacleProbability = 0.30; // 40% chance of obstacle

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        long[] totalTime = {0}; // Store totalTime in an array

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < numTests; i++) {
            final int testIndex = i + 1; // Make testIndex effectively final
            final int[][] grid = RandomGridGenerator.generateRandomGrid(rows, cols, obstacleProbability);

            tasks.add(() -> {
                long startTime = System.nanoTime();

                Node start = new Node(0, 0);
                Node goal = new Node(rows - 1, cols - 1);

                System.out.println("Generated Grid " + testIndex + ":");
                //RandomGridGenerator.printGrid(grid);

                //System.out.println("Start: " + start.toString() + "\nGoal: " + goal.toString());

                List<Node> path = AStar.aStar(start, goal, grid);

                long endTime = System.nanoTime();
                long duration = endTime - startTime;
                totalTime[0] += duration; // Update totalTime

                if (path.isEmpty()) {
                    failureCount.incrementAndGet();
                    //System.out.println("No path found for Grid " + testIndex);
                } else {
                    successCount.incrementAndGet();
                    //System.out.println("Path found for Grid " + testIndex + ":");
                    //RandomGridGenerator.printGrid(grid, path);
                }
                
                return null;
            });
        }

        try {
            List<Future<Void>> results = executor.invokeAll(tasks);
            executor.shutdown();

            for (Future<Void> result : results) {
                result.get(); // Ensure all tasks are completed
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        double successRate = (double) successCount.get() / numTests * 100;
        double failureRate = (double) failureCount.get() / numTests * 100;
        double averageExecutionTime = (double) totalTime[0] / numTests / 1_000_000; // Convert nanoseconds to milliseconds

        System.out.println("Successes: " + successCount.get());
        System.out.println("Failures: " + failureCount.get());
        System.out.println("Success Rate: " + successRate + "%");
        System.out.println("Failure Rate: " + failureRate + "%");
        System.out.println("Average Execution Time (ms): " + averageExecutionTime);
        
        // Print total time elapsed
        long totalSeconds = TimeUnit.NANOSECONDS.toSeconds(totalTime[0]);
        System.out.println("Total Time Elapsed: " + totalSeconds + " seconds");
    }
}
