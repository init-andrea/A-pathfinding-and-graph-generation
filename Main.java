import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int rows = 20;
        int cols = 20;
        double obstacleProbability = 0.3; // 30% chance of obstacle

        int[][] grid = RandomGridGenerator.generateRandomGrid(rows, cols, obstacleProbability);

        System.out.println("Generated Grid:");
        RandomGridGenerator.printGrid(grid);
        
        Random random = new Random();
        Node start = new Node(random.nextInt(rows - 1), random.nextInt(cols - 1));
        Node goal = new Node(random.nextInt(rows - 1), random.nextInt(cols - 1));
        List<Node> path = AStar.aStar(start, goal, grid);

        if (path.isEmpty()) {
            System.out.println("No path found.");
        } else {
            System.out.println("Path found:");
            for (Node node : path) {
                System.out.println("Node: (" + node.x + ", " + node.y + ")");
            }
        }
    }
}