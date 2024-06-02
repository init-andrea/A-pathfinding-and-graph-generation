
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AStar {

    public Result findPath(Node start, Node goal) {
        PriorityQueue<NodeComp> openSet = new PriorityQueue<>();    // nodi da esplorare
        Map<Node, NodeComp> closedSet = new HashMap<>();            // nodi esplorati

        NodeComp startComp = new NodeComp(start, null, 0, Utilities.euclideanDistance(start, goal));

        // nodi ancora da esplorare
        openSet.add(startComp);

        while (!openSet.isEmpty()) {
            NodeComp next = openSet.poll();

            // se abbiamo già esplorato il nodo
            if (closedSet.containsKey(next.getNode())) {
                continue;
            }

            // se siamo arrivati all'obiettivo
            if (next.getNode().equals(goal)) {
                List<Node> path = new ArrayList<>();
                NodeComp current = next;
                double totalPathCost = current.getCostSoFar();

                // ripercorriamo il percorso all'indietro
                do {
                    path.add(current.getNode());
                    current = closedSet.get(current.getPredecessor());
                } while (current != null);

                // invertiamo il percorso per andare da start a goal
                Collections.reverse(path);
                return new Result(path, totalPathCost);
            }

            // se non siamo arrivati all'obiettivo
            for (Edge edge : next.getNode().getEdges()) {
                Node toNode = edge.getDest();
                double costSoFar = next.getCostSoFar() + edge.getCost();
                double estimatedCostTotal = costSoFar + Utilities.euclideanDistance(toNode, goal);

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

}
