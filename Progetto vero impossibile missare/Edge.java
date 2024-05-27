public class Edge {
    private Node from, to;
    private double cost;

    public Edge(Node from, Node to, double cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    public Node getFrom() {return from;}
    public Node getTo() {return to;}
    public double getCost() {return cost;}
}
