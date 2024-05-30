
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Experiments {

    // scegliamo a tempo di esecuzione il numero di esperimenti che vogliamo eseguire, il numero di nodi e la probabilità per ogni grafo generato
    public static void main(String[] args) {
        int numberOfExperiments;
        int successfulExperiments = 0;
        int failedExperiments = 0;
        int numberOfNodes;
        double probability;
        Node start, goal;
        Scanner userInput = new Scanner(System.in);
        Random rand = new Random(System.currentTimeMillis());
        AStar aStar = new AStar();

        // leggiamo il numero di esperimenti da eseguire
        System.out.println("Inserisci il numero di esperimenti da eseguire e premi invio: ");
        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Riprova, devi inserire un numero intero");
        }
        numberOfExperiments = userInput.nextInt();
        /*
        // leggiamo il numero di nodi del grafo da generare
        System.out.println("Inserisci il numero di nodi del grafo e premi invio: ");
        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Riprova, devi inserire un numero intero");
        }
        numberOfNodes = userInput.nextInt();

        // leggiamo la probabilità di generare un arco tra due vertici     
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
         */
        long startExperimentTime = System.nanoTime();
        userInput.close();
        int numberOfConnectedGraphs = 0;
        long resAStar = 0;

        for (int counter = 0; counter < numberOfExperiments; counter++) {

            // generiamo il grafo a partire dalla probabilità e numero di nodi dati
            //Node[] nodes = Utilities.generateErdosRenyiGraph(numberOfNodes, probability);
            Node[] nodes;
            try {
                nodes = GraphUtils.loadGraph("Nodes.txt", "Edges.txt");
            } catch (IOException e) {
                return;
            }
            //Utilities.printGraph(nodes);
            int counterArchi = 0;
            for (Node node : nodes) {
                counterArchi += node.getEdges().size();
            }
            System.out.println(counterArchi);
            if (Utilities.isConnected(nodes)) {
                numberOfConnectedGraphs++;
            }

            // scegliamo a caso, tra i nodi del grafo, start e goal su cui far girare A*. Li cerchiamo non isolati
            /*
            do { 
                start = nodes[rand.nextInt(nodes.length)];
                if (start.getEdges().isEmpty()) System.out.println(start.getLabel());
            } while (!start.getEdges().isEmpty());
            do {
                goal = nodes[rand.nextInt(nodes.length)];
            } while (start == goal && !goal.getEdges().isEmpty());             // per assicurarci che start e goal non siano lo stesso nodo
             */
            start = nodes[rand.nextInt(nodes.length)];
            //System.out.println("start: "+start.getLabel());
            do {
                goal = nodes[rand.nextInt(nodes.length)];
            } while (start == goal);
            //System.out.println("goal: "+goal.getLabel());

            long startTime = System.nanoTime();
            List<Node> bestPath = aStar.findPath(start, goal);
            long endTime = System.nanoTime();

            if (bestPath != null) {
                System.out.println("Percorso da " + start.getLabel() + " a " + goal.getLabel() + " trovato: ");
                successfulExperiments++;
                for (Node n : bestPath) {
                    //System.out.print(n.getLabel() + " ");
                }
                //System.out.println();
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
        //String resultsData = "" + numberOfExperiments + " " + numberOfNodes + " " + probability + " " + successfulExperiments + " " + numberOfConnectedGraphs + " " + totalTime;
        //System.out.println(resultsData);
        //String aStarResultsData = "" + numberOfExperiments + " " + numberOfNodes + " " + probability + " " + (resAStar/numberOfExperiments);
        //Utilities.writeToFile("Risultati.txt", resultsData);
        //Utilities.writeToFile("RisultatiAStar.txt", aStarResultsData);
    }
}
