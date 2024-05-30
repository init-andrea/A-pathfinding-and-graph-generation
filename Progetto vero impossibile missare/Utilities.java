
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Utilities {

    // distanza euclidea come euristica nel caso generale, può essere Manhattan se il grafo è una griglia
    // calcola la distanza euclidea tra due nodi che hanno coordinate x e y
    public static double euclideanDistance(Node a, Node b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.hypot(dx, dy);
    }

    // p è la probabilità di avere un arco tra due vertici (p=1 grafo denso), n è il numero dei vertici
    public static Node[] generateErdosRenyiGraph(int n, double p){  
        
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

        // scelta maxX e maxY con Random
        Random rand = new Random(System.currentTimeMillis());
        double maxX = rand.nextDouble(1000);
        double maxY = rand.nextDouble(1000);

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(String.valueOf(i), rand.nextDouble(maxX), rand.nextDouble(maxY));
        }
        for (int j = 0; j < n; j++) {
            for (int k = j + 1; k < n; k++) {
                if (rand.nextDouble() < p) {                                    // scegliamo se creare o no un arco in base alla probabilità p
                    Node nj = nodes[j];
                    Node nk = nodes[k];
                    double cost = euclideanDistance(nj, nk);        // calcoliamo il costo dell'arco come distanza euclidea tra i due vertici
                    if (!nj.hasEdge(nk) && !nk.hasEdge(nj)) {
                        nodes[j].addEdge(nk, cost);
                        nodes[k].addEdge(nj, cost);                           // aggiungiamo un arco in entrambe le direzioni per un grafo doppiamente orientato/non orientato
                    }
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
        //isConnected(nodes);
    }

    // controlla se il grafo è connesso cercando nodi isolati (funziona per grafi non orientati/doppiamente orientati)
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

    public static void writeToFile(String nomeFile, String testo){
        try {
            File file = new File(nomeFile);
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Il file " + nomeFile + " è stato creato");
            }
            FileWriter writer = new FileWriter(nomeFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write(testo);
            bufferedWriter.newLine();
            bufferedWriter.close();
            //System.out.println("Scrittura completata");
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file: " + e.getMessage());
        }
    }

    /*
    // test main class for graph generation
    public static void main(String[] args) {
        int iterations = 10000;
        int connessoCounter = 0;
        for (int i = 0; i < iterations; i++) {
            if(isConnected(generateErdosRenyiGraph(200, 0.015))){
                connessoCounter++;
            }
        }
        //printGraph(generateErdosRenyiGraph(20, 0.2));
        System.out.println(connessoCounter + " grafi connessi su " + iterations + ", il " + (((double)connessoCounter/iterations)*100) + "%");
    } 
    */

}
