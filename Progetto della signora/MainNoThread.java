import java.util.List;

public class MainNoThread {
    public static void main(String[] args) {
        int numTests = 1000; // Number of random grids to test
        int rows = 100;
        int cols = 100;
        double obstacleProbability = 0.30; // Chance of obstacle

        int successCount = 0;
        int failureCount = 0;

        for (int i = 0; i < numTests; i++) {
            int[][] grid = RandomGridGenerator.generateRandomGrid(rows, cols, obstacleProbability);

            //System.out.println("Generated Grid " + (i + 1) + ":");
            //RandomGridGenerator.printGrid(grid);

            Node start = new Node(0, 0);
            Node goal = new Node(rows - 1, cols - 1);

            //System.out.println("Start: " + start.toString() + "\nGoal: " + goal.toString());

            List<Node> path = AStar.aStar(start, goal, grid);

            if (path.isEmpty()) {
                failureCount++;
                System.out.println("No path found for Grid " + (i + 1));
            } else {
                successCount++;
                System.out.println("Path found for Grid " + (i + 1) + ":");
                //RandomGridGenerator.printGrid(grid, path);
            }

            System.out.println();
        }

        double successRate = (double) successCount / numTests * 100;
        double failureRate = (double) failureCount / numTests * 100;

        System.out.println("Successes: " + successCount);
        System.out.println("Failures: " + failureCount);
        System.out.println("Success Rate: " + successRate + "%");
        System.out.println("Failure Rate: " + failureRate + "%");
    }
}
