import java.util.List;

public class Result {
        private List<Node> path;
        private double totalCost;

        public Result(List<Node> path, double totalCost) {
            this.path = path;
            this.totalCost = totalCost;
        }

        public List<Node> getPath() {
            return path;
        }

        public double getTotalCost() {
            return totalCost;
        }
    }
