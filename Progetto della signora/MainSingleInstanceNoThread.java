import java.util.List;

public class MainSingleInstanceNoThread {
    public static void main(String[] args) {
        int rows = 30;
        int cols = 100;
        double obstacleProbability = 0.30; // 30% chance of obstacle

        int[][] grid = RandomGridGenerator.generateRandomGrid(rows, cols, obstacleProbability);

        System.out.println("Generated Grid:");
        RandomGridGenerator.printGrid(grid);
        
        /*
         * Random random = new Random();
        Node start = new Node(random.nextInt(rows - 1), random.nextInt(cols - 1));
        Node goal = new Node(random.nextInt(rows - 1), random.nextInt(cols - 1));
        */

        Node start = new Node(0,0);
        Node goal = new Node(rows - 1, cols - 1);
        System.out.println("Start: " + start.toString() + "\nGoal: " + goal.toString());

        List<Node> path = AStar.aStar(start, goal, grid);

        if (path.isEmpty()) {
            System.out.println("No path found.");
        } else {
            System.out.println("Path found:");
            RandomGridGenerator.printGrid(grid, path);
            /*for (Node node : path) {
                System.out.println("Node: (" + node.x + ", " + node.y + ")");
            }*/
        }

    }
}
