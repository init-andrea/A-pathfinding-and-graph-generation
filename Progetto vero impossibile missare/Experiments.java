
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Experiments {

    // scegliamo a tempo di esecuzione il numero di esperimenti che vogliamo eseguire, il numero di nodi e la probabilità per ogni grafo generato
    public static void main(String[] args) {

        // variabili per l'input dell'utente
        int numberOfExperiments;
        int numberOfNodes = 0;
        boolean readGraph = true;           // se true leggo grafo da Nodes.txt ed Edges.txt, altrimenti chiedo numero di nodi e genero il grafo con Erdos-Renyi
        double probability = 0.0;
        String nodesFile = "";
        String edgesFile = "";
        Scanner userInput = new Scanner(System.in);

        int numberOfEdges = 0;
        int successfulExperiments = 0;
        int failedExperiments = 0;
        Node start, goal;
        
        Random rand = new Random(System.currentTimeMillis());
        AStar aStar = new AStar();

        // scelta del grafo da usare
        System.out.println("Scegli che grafo usare (scrivi 1 o 2):\n1- lettura da file\n2- generazione tramite Erdos-Renyi");
        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Riprova, devi inserire 1 o 2:\n1- lettura da file\n2- generazione tramite Erdos-Renyi");
        }
        int userValue = userInput.nextInt();
        if (userValue != 1 && userValue != 2) {
            userInput.close();
            System.out.println("Il numero inserito deve essere 1 o 2");
            return;
        }
        if (userValue == 2) {
            readGraph = false;
        }

        // leggiamo il nome dei file in input se si sceglie la prima opzione
        if (readGraph) {
            System.out.println("Scrivi il nome del file con i nodi: ");
            nodesFile = userInput.next();
            System.out.println("Scrivi il nome del file con gli archi: ");
            edgesFile = userInput.next();
        }

        // leggiamo il numero di nodi del grafo da generare se si sceglie la seconda opzione
        if (!readGraph) {
            System.out.println("Inserisci il numero di nodi del grafo e premi invio: ");
            while (!userInput.hasNextInt()) {
                userInput.next();
                System.out.println("Riprova, devi inserire un numero intero");
            }
            numberOfNodes = userInput.nextInt();
        }

        // leggiamo il numero di esperimenti da eseguire
        System.out.println("Inserisci il numero di esperimenti da eseguire e premi invio: ");
        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Riprova, devi inserire un numero intero");
        }
        numberOfExperiments = userInput.nextInt();

        // leggiamo la probabilità di generare un arco tra due vertici, serve per entrambi i grafi
        System.out.println("Inserisci la probabilità (compresa tra 0 e 1) di generare un arco tra due vertici e premi invio:");
        while (true) {
            if (userInput.hasNextDouble()) {
                probability = userInput.nextDouble();
                if (probability >= 0 && probability <= 1) {
                    break;
                } else {
                    System.out.println("Riprova, devi inserire un numero compreso tra 0 e 1");
                }
            } else {
                System.out.println("Riprova, devi inserire un numero");
                userInput.next();       // togliamo l'input che non è valido dallo scanner
            }
        }

        long startExperimentTime = System.nanoTime();
        userInput.close();
        int numberOfConnectedGraphs = 0;
        long resAStar = 0;
        Node[] nodes;

        // leggiamo il grafo dai file Nodes.txt ed Edges.txt
        // lo carichiamo in memoria una volta prima di iniziare il loop perché il grafo letto non cambia con gli esperimenti, come invece succede con quello generato
        try {
            nodes = Utilities.loadGraphFromFiles(nodesFile, edgesFile, probability);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        numberOfNodes = nodes.length;

        for (int counter = 0; counter < numberOfExperiments; counter++) {

            // generiamo il grafo a partire dalla probabilità e numero di nodi dati
            if (!readGraph){
                nodes = Utilities.generateErdosRenyiGraph(numberOfNodes, probability);
            }
            
            //Utilities.printGraph(nodes);

            for (Node node : nodes) {
                numberOfEdges += node.getEdges().size();
            }
            System.out.println(numberOfEdges + " archi totali");

            if (Utilities.isConnected(nodes)) {
                numberOfConnectedGraphs++;          // contare grafi connessi così dovrebbe funzionare solo per grafi non orientati/doppiamente orientati
            }

            /*
            // scegliamo a caso, tra i nodi del grafo, start e goal. Li cerchiamo non isolati
            do { 
                start = nodes[rand.nextInt(nodes.length)];
                if (start.getEdges().isEmpty()) System.out.println(start.getLabel());
            } while (start.getEdges().isEmpty());
            do {
                goal = nodes[rand.nextInt(nodes.length)];
            } while (start == goal && goal.getEdges().isEmpty());             // per assicurarci che start e goal non siano lo stesso nodo
            */

            // scegliamo a caso, tra i nodi del grafo, start e goal
            start = nodes[rand.nextInt(nodes.length)];
            //System.out.println("start: "+start.getLabel());
            do {
                goal = nodes[rand.nextInt(nodes.length)];
            } while (start == goal);
            //System.out.println("goal: "+goal.getLabel());
            

            long startTime = System.nanoTime();
            List<Node> bestPath = aStar.findPath(start, goal);              // TODO mettere counter di quanti nodi vengono visitati
            long endTime = System.nanoTime();

            if (bestPath != null) {
                System.out.println("Percorso da " + start.getLabel() + " a " + goal.getLabel() + " trovato: ");
                successfulExperiments++;
                for (Node n : bestPath) {
                    System.out.print(n.getLabel() + " ");
                    // TODO aggiungere peso totale alla print
                }
                System.out.println();
            } else {
                //System.out.println("Nessun percorso trovato");
                failedExperiments++;
            }

            //System.out.println("Tempo di esecuzione esperimento " + counter + " : " + (endTime - startTime) + " ns");
            resAStar += (endTime - startTime);
        }
        long endExperimentTime = System.nanoTime();
        long totalTime = endExperimentTime - startExperimentTime;
        System.out.println("Esperimenti che hanno avuto successo: " + successfulExperiments + ", " + ((((double) successfulExperiments / numberOfExperiments) * 100)) + "%");
        System.out.println("Esperimenti che sono falliti: " + failedExperiments + ", " + ((((double) failedExperiments / numberOfExperiments) * 100)) + "%");
        System.out.println("Grafi connessi: " + numberOfConnectedGraphs + ", il " + ((double) numberOfConnectedGraphs / numberOfExperiments * 100) + "%");
        System.out.println("Tempo totale: " + (totalTime / 1000000) + " ms. Tempo di esecuzione medio: " + totalTime / numberOfExperiments + " ns");
        String resultsData = "" + numberOfExperiments + " " + numberOfNodes + " " + numberOfEdges + " " + probability + " " + successfulExperiments + " " + numberOfConnectedGraphs + " " + totalTime;
        //System.out.println(resultsData);
        //String aStarResultsData = "" + numberOfExperiments + " " + numberOfNodes + " " + probability + " " + (resAStar/numberOfExperiments);
        if (readGraph) {
            Utilities.writeToFile("Risultati" + nodesFile.substring(5), resultsData);
        } else {
            Utilities.writeToFile("Risultati.txt", resultsData);
        }
        
        //Utilities.writeToFile("RisultatiAStar.txt", aStarResultsData);
    }
}
