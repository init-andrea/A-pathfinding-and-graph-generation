import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AStar {

    public List<Node> findPath(Node start, Node goal){
        PriorityQueue<NodeComp> openSet = new PriorityQueue<>();
        Map<Node, NodeComp> closedSet = new HashMap<>();

        NodeComp startNode = new NodeComp(start, null, 0, heuristic(start, goal));
        
        // nodi ancora da esplorare
        openSet.add(startNode);

        // nodi già esplorati
        closedSet.put(start, startNode);

        while (!openSet.isEmpty()) { 
            NodeComp next = openSet.poll();

            // se abbiamo già esplorato il nodo
            if (closedSet.containsKey(next.getNode())) {
                continue;
            }

            // se siamo arrivati all'obiettivo
            if (next.getNode().equals(goal)){
                List<Node> path = new ArrayList<>();
                NodeComp current = next;

                // ripercorriamo il percorso all'indietro
                do { 
                    path.add(current.getNode());
                    current = closedSet.get(current.getPredecessor());
                } while (current != null);

                // invertiamo il percorso per andare da start a goal
                Collections.reverse(path);
                return path;
            }

            // se non siamo arrivati all'obiettivo
            for (Edge edge : next.getNode().getEdges()){
                Node toNode = edge.getTo();
                double costSoFar = next.getCostSoFar() + edge.getCost();
                double estimatedCostTotal = costSoFar + heuristic(toNode, goal);

                if (closedSet.containsKey(toNode)) {
                    continue;
                }

                NodeComp toComp = new NodeComp(toNode, next.getNode(), costSoFar, estimatedCostTotal);
                openSet.add(toComp);
            }

            // abbiamo iterato tutti gli archi di next, quindi lo mettiamo nel closedSet
            closedSet.put(next.getNode(), next);
        }

        // non c'è nessun percorso da start a goal
        return null;
    }

    // distanza euclidea come euristica nel caso generale, può essere Manhattan se il grafo è una griglia
    private double heuristic(Node a, Node b) {
        double xS = a.getX();
        double yS = a.getY();
        double xG = b.getX();
        double yG = b.getY();
        return Math.sqrt((xG-xS) * (xG-xS) + (yG-yS) * (yG-yS));
    }
}