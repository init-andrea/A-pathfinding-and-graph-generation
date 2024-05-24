import java.util.List;
import java.util.Random;

public class RandomGridGenerator {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    
    public static int[][] generateRandomGrid(int rows, int cols, double obstacleProbability) {
        int[][] grid = new int[rows][cols];
        Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (random.nextDouble() < obstacleProbability) {
                    grid[i][j] = 1; // obstacle
                } else {
                    grid[i][j] = 0; // free space
                }
            }
        }

        // Ensure the start and goal positions are free of obstacles
        grid[0][0] = 0;
        grid[rows - 1][cols - 1] = 0;

        return grid;
    }

    public static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == 0) {
                    System.out.print(ANSI_GREEN + "■" + ANSI_RESET + " ");
                } else {
                    System.out.print(ANSI_RED + "■" + ANSI_RESET +" ");
                }
            }
            System.out.println();
        }
    }

    public static void printGrid(int[][] grid, List<Node> path) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                Node node = new Node(i, j);
                if (path.contains(node)) {
                    System.out.print(ANSI_YELLOW + "■" + ANSI_RESET + " ");
                } else if (grid[i][j] == 0) {
                    System.out.print(ANSI_GREEN + "■" + ANSI_RESET + " ");
                } else {
                    System.out.print(ANSI_RED + "■" + ANSI_RESET + " ");
                }
            }
            System.out.println();
        }
    }
}