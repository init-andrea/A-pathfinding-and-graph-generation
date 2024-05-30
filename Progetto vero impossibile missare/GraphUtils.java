import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GraphUtils {

    public static Node[] loadGraph(String nodesFilePath, String edgesFilePath) throws IOException {
        Map<String, Node> nodeMap = new HashMap<>();
        Random random = new Random();
        
        // Leggi il file Nodes.txt
        try (BufferedReader br = new BufferedReader(new FileReader(nodesFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                String nodeId = parts[0];
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                Node node = new Node(nodeId, x, y);
                nodeMap.put(nodeId, node);
            }
        }

        // Leggi il file Edges.txt
        try (BufferedReader br = new BufferedReader(new FileReader(edgesFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                //String edgeId = parts[0];
                String startNodeId = parts[1];
                String endNodeId = parts[2];
                double distance = Double.parseDouble(parts[3]);

                Node startNode = nodeMap.get(startNodeId);
                Node endNode = nodeMap.get(endNodeId);
                double probability = 1;                   // la probabilità di creare un secondo arco per grafo non orientato

                if (startNode != null && endNode != null) {
                    startNode.addEdge(endNode, distance);
                    if (random.nextDouble() < probability) {
                        endNode.addEdge(startNode, distance);
                    }               // per avere grafi non orientati
                }
            }
        }
        int counterIsolati = 0;
        for (Node node : nodeMap.values()) {
            if (node.getEdges().isEmpty()) {
                counterIsolati++;
            }
        }
        System.out.print(counterIsolati + " isolati, ");
        /*
        // Crea una lista di nodi senza nodi isolati
        List<Node> nodesWithEdges = new ArrayList<>();
        int counter = 0;
        int totCounter = 0;
        for (Node node : nodeMap.values()) {
            if (!node.getEdges().isEmpty()) {
                nodesWithEdges.add(node);
                counter++;
                //System.out.println(counter);
            }
            totCounter++;
            //System.out.println(totCounter);
            if (totCounter==nodeMap.size()-1) break;
        }

        // Converti la lista in un array di Node
        Node[] nodesArray = nodesWithEdges.toArray(new Node[0]);
        return nodesArray;
        */
        Node[] nodesArray = nodeMap.values().toArray(new Node[0]);
        return nodesArray;

    }

    public static void main(String[] args) {
        try {
            Node[] nodes = loadGraph("Nodes.txt", "Edges.txt");
            // Stampa informazioni sui nodi e sugli archi per verificare
            /*
            for (Node node : nodes) {
                System.out.println("Node " + node.getLabel() + " (" + node.getX() + ", " + node.getY() + ")");
                for (Edge edge : node.getEdges()) {
                    System.out.println("  Edge to " + edge.getDest().getLabel() + " with cost " + edge.getCost());
                }
            }
            */
            Utilities.printGraph(nodes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
