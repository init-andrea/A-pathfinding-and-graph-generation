import java.util.*;

public class AStar {
    private static final int[] DX = {-1, 1, 0, 0};
    private static final int[] DY = {0, 0, -1, 1};

    public static List<Node> aStar(Node start, Node goal, int[][] grid) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        Set<Node> closedList = new HashSet<>();
        start.g = 0;
        start.h = heuristic(start, goal);
        openList.add(start);

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            if (current.equals(goal)) {
                return constructPath(current);
            }

            closedList.add(current);

            for (int i = 0; i < 4; i++) {
                int newX = current.x + DX[i];
                int newY = current.y + DY[i];
                if (isValid(newX, newY, grid)) {
                    Node neighbor = new Node(newX, newY);
                    if (closedList.contains(neighbor)) {
                        continue;
                    }

                    double tentativeG = current.g + 1;

                    if (!openList.contains(neighbor) || tentativeG < neighbor.g) {
                        neighbor.g = tentativeG;
                        neighbor.h = heuristic(neighbor, goal);
                        neighbor.parent = current;
                        if (!openList.contains(neighbor)) {
                            openList.add(neighbor);
                        }
                    }
                }
            }
        }

        return Collections.emptyList();  // Return an empty list if no path is found
    }

    private static boolean isValid(int x, int y, int[][] grid) {
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length && grid[x][y] == 0;
    }

    private static double heuristic(Node a, Node b) {
        // Using Manhattan distance as the heuristic
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private static List<Node> constructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }
}
