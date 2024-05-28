public class Edge {
    private Node src, dest;
    private double cost;

    public Edge(Node src, Node dest, double cost) {
        this.src = src;
        this.dest = dest;
        this.cost = cost;
    }

    public Node getSrc() {return src;}
    public Node getDest() {return dest;}
    public double getCost() {return cost;}
}