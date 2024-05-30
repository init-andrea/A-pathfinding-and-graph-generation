import java.util.ArrayList;
import java.util.List;

public class Node {
    private String label;
    private double x, y;
    private List<Edge> edges;

    // costruttore per inizializzare nodo senza archi
    public Node(String label, double x, double y) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.edges = new ArrayList<>();
    }

    // costruttore per inizializzare nodo con archi
    public Node(String label, double x, double y, List<Edge> edges) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.edges = edges;
    }

    public boolean hasEdge(Node to){
        for (Edge edge : edges) {
            if (edge.getDest().equals(to)) {
                return true;
            }
        }
        return false;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public String getLabel(){
        return this.label;
    }

    public void addEdge(Node to, double cost){
        edges.add(new Edge(this, to, cost));
    }

    public void setEdges(List<Edge> edges){
        this.edges = edges;
    }

    public List<Edge> getEdges(){
        return this.edges;
    }

}