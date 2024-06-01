
public class NodeComp implements Comparable<NodeComp> {     // nodeComparable, serve implementare Comparable per l'ordine della PriorityQueue

    private Node node;
    private Node predecessor;
    private double costSoFar;
    private double costEstimate;

    public NodeComp(Node node, Node predecessor, double costSoFar, double costEstimate) {
        this.node = node;
        this.predecessor = predecessor;
        this.costSoFar = costSoFar;
        this.costEstimate = costEstimate;
    }

    public Node getNode() {
        return this.node;
    }

    public Node getPredecessor() {
        return this.predecessor;
    }

    public double getCostSoFar() {
        return this.costSoFar;
    }

    public double getCostEstimate() {
        return this.costEstimate;
    }

    @Override
    public int compareTo(NodeComp other) {
        if (this.costEstimate > other.costEstimate) {
            return 1;
        } else if (this.costEstimate < other.costEstimate) {
            return -1;
        } else {
            return 0;
        }
    }

}
