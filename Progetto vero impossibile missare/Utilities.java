
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Utilities {

    static final double maxXBound = 1000;
    static final double maxYBound = 1000;

    public static String[] getInputFileNames(Scanner userInput){
        String[] nomiFile = new String[2];
        System.out.println("Scrivi il nome del file con i nodi: ");
        nomiFile[0] = userInput.next();
        System.out.println("Scrivi il nome del file con gli archi: ");
        nomiFile[1] = userInput.next();
        return nomiFile;
    }

    public static int getInputNodesNumber(Scanner userInput){
        System.out.println("Inserisci il numero di nodi del grafo e premi invio: ");
        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Riprova, devi inserire un numero intero");
        }
        return userInput.nextInt();
    }
    
    public static int getInputExperimentsNumber(Scanner userInput){
        System.out.println("Inserisci il numero di esperimenti da eseguire e premi invio: ");
        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Riprova, devi inserire un numero intero");
        }
        return userInput.nextInt();
    }
    
    public static double getInputProbability(Scanner userInput){
        double probabilità;
        System.out.println("Inserisci la probabilità (compresa tra 0 e 1) di generare un arco tra due vertici e premi invio:");
        while (true) {
            if (userInput.hasNextDouble()) {
                probabilità = userInput.nextDouble();
                if (probabilità >= 0 && probabilità <= 1) {
                    return probabilità;
                } else {
                    System.out.println("Riprova, devi inserire un numero compreso tra 0 e 1");
                }
            } else {
                System.out.println("Riprova, devi inserire un numero");
                userInput.next();       // togliamo l'input che non è valido dallo scanner
            }
        }
    }
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
        double maxX = rand.nextDouble(maxXBound);
        double maxY = rand.nextDouble(maxYBound);

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
    }

    public static void writeResultToFIle(String nomeFile, String testo){
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

    public static void writeErdosRenyiGraphToFile(Node[] nodes, double probabilità){
        int numeroNodi = nodes.length;
        int numeroArchi = 0;
        
        for (Node node : nodes) {
            numeroArchi += node.getEdges().size();
        }
        
        String nomeFileNodi = "Nodes_Erdos_Renyi" + numeroNodi + "V" + probabilità + "P.txt";
        String nomeFileArchi = "Edges_Erdos_Renyi" + numeroArchi + "E" + probabilità + "P.txt";

        try {
            File fileNodi = new File(nomeFileNodi);
            File fileArchi = new File(nomeFileArchi);
            if (!fileNodi.exists()) {
                fileNodi.createNewFile();
                System.out.println("Il file " + nomeFileNodi + " è stato creato");
            }
            if (!fileArchi.exists()) {
                fileArchi.createNewFile();
                System.out.println("Il file " + nomeFileArchi + " è stato creato");
            }
            FileWriter nodesWriter = new FileWriter(nomeFileNodi, true);
            BufferedWriter nodesBufferedWriter = new BufferedWriter(nodesWriter);
            FileWriter edgesWriter = new FileWriter(nomeFileArchi, true);
            BufferedWriter edgesBufferedWriter = new BufferedWriter(edgesWriter);

            for (Node node : nodes) {
                nodesBufferedWriter.write(node.getLabel() + " " + node.getX() + " " + node.getY());
                nodesBufferedWriter.newLine();
                int counter = 0;
                for (Edge edge : node.getEdges()) {
                    edgesBufferedWriter.write("" + (counter+1) + " " + edge.getSrc().getLabel() + " " + edge.getDest().getLabel() + " " + edge.getCost());
                    edgesBufferedWriter.newLine();
                }
            }
            nodesBufferedWriter.close();
            edgesBufferedWriter.close();
            //System.out.println("Scrittura completata");
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file: " + e.getMessage());
        }
    }

    public static Node[] loadGraphFromFiles(String nodesFilePath, String edgesFilePath, double probability) throws IOException {
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
        } catch(FileNotFoundException e){
            throw e;
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

                if (startNode != null && endNode != null) {
                    startNode.addEdge(endNode, distance);
                    //double pp = random.nextDouble();
                    //System.out.println(pp);
                    
                    if (random.nextDouble() <= probability) {        // la probabilità di creare un secondo arco per grafo non orientato
                        endNode.addEdge(startNode, distance);
                    }
                    
                    //endNode.addEdge(startNode, distance);
                }
            }
        } catch (FileNotFoundException e) {
            throw e;
        }

        int counterIsolati = 0;
        for (Node node : nodeMap.values()) {
            if (node.getEdges().isEmpty()) {
                counterIsolati++;
            }
        }
        //System.out.print(counterIsolati + " isolati, ");
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

}
