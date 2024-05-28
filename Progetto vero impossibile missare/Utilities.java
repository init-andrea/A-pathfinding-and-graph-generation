
import java.util.Random;

public class Utilities {
    public static double euclideanDistance(Node a, Node b) {
        double xS = a.getX();
        double yS = a.getY();
        double xG = b.getX();
        double yG = b.getY();
        return Math.sqrt((xG-xS) * (xG-xS) + (yG-yS) * (yG-yS));
    }

    // p è la probabilità di avere un arco tra due vertici (p=1 grafo denso), n è il numero dei vertici
    public static Node[] generateErdosRenyiGraph(double p, int n){  
        
        // Check sugli input
        if (p <= 0 || p > 1) {
            System.out.println("La probabilità di avere un arco tra due vertici deve essere (0,1]");
            return null;
        }
        if (n <= 0) {
            System.out.println("Il numero di vertici deve essere positivo");
            return null;
        }

        // lettura maxX e maxY da console
        /*Scanner userInput = new Scanner(System.in);
        System.out.println("Inserisci la coordinata massima sull'asse x: ");
        while (!userInput.hasNextDouble()) {
            userInput.next();
            System.out.println("Riprova, devi inserire un numero");
        }
        double maxX = userInput.nextDouble();
        System.out.println("Inserisci la coordinata massima sull'asse y: ");
        while (!userInput.hasNextDouble()) {
            userInput.next();
            System.out.println("Riprova, devi inserire un numero");
        }
        double maxY = userInput.nextDouble();
        userInput.close();*/
        Random rand = new Random(System.currentTimeMillis());
        double maxX = rand.nextDouble(500);
        double maxY = rand.nextDouble(500);

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(String.valueOf(i), rand.nextDouble(maxX), rand.nextDouble(maxY));
        }
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < n; k++) {
                if (rand.nextDouble() < p) {                                    // scegliamo se creare o no un arco in base alla probabilità p
                    double cost = euclideanDistance(nodes[j], nodes[k]);        // calcoliamo il costo dell'arco come distanza euclidea tra i due vertici
                    nodes[j].addEdge(nodes[k], cost);
                    nodes[k].addEdge(nodes[j], cost);                           // aggiungiamo un arco in entrambe le direzioni per un grafo doppiamente orientato/non orientato
                }
            }
        }
        return nodes;
    }

    public static void printGraph(Node[] nodes){
        for (Node node : nodes) {
            System.out.print(node.getLabel() + " (" + node.getX() + ", " + node.getY() + "): ");
            for (Edge edge : node.getEdges()) {
                System.out.print("->" + edge.getDest().getLabel() + " (cost: " + edge.getCost() + ")");
            }
            System.out.println();
        }
        isConnected(nodes);
    }

    public static boolean isConnected(Node[] nodes){
        for (Node node : nodes) {
            if (node.getEdges().isEmpty()) {
                //System.out.println("Il grafo non è connesso");
                return false;
            }
        }
        //System.out.println("Il grafo è connesso");
        return true;
    }

    // test main class for graph generation
    public static void main(String[] args) {
        int iterations = 10000;
        int connessoCounter = 0;
        for (int i = 0; i < iterations; i++) {
            if(isConnected(generateErdosRenyiGraph(0.015, 200))){
                connessoCounter++;
            }
        }
        //printGraph(generateErdosRenyiGraph(0.2, 20));
        System.out.println(connessoCounter + " grafi connessi su " + iterations + ", il " + (((double)connessoCounter/iterations)*100) + "%");
    }

}
