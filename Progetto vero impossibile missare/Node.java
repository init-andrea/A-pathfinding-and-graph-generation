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

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public void addEdge(Node to, double cost){
        edges.add(new Edge(this, to, cost));
    }

    public List<Edge> getEdges(){
        return this.edges;
    }

}